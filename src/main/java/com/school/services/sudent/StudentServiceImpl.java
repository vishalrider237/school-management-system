package com.school.services.sudent;

import com.school.dto.*;
import com.school.entities.StudentLeave;
import com.school.entities.User;
import com.school.enums.StudentLeaveStatus;
import com.school.repositories.FeeRepository;
import com.school.repositories.StudentLeaveRepositories;
import com.school.repositories.UserRepositories;

import com.school.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {
   static HashMap<String,String>otpstore=new HashMap<>();
    @Autowired
    private UserRepositories userRepositories;

    @Autowired
    private StudentLeaveRepositories studentLeaveRepositories;

    @Autowired
    private FeeRepository feeRepository;

    @Override
    public ResponseEntity<?> getStudentbyId(Long id) {
         Optional<User> user = userRepositories.findById(id);
        Optional<StudentDto> studentDTO=user.map(this::mapToDTO);
        return ResponseEntity.ok(studentDTO.get());
    }
    private StudentDto mapToDTO(User user) {
        Boolean feestatus=this.feeRepository.getFeeStatus(user.getId());
        StudentDto studentDto=new StudentDto();
        studentDto.setId(user.getId());
        studentDto.setEmail(user.getEmail());
        studentDto.setName(user.getName());
        studentDto.setGender(user.getGender());
        studentDto.setDateOfBirth(user.getDateOfBirth());
        studentDto.setAddress(user.getAddress());
        studentDto.setFatherName(user.getFatherName());
        studentDto.setMotherName(user.getMotherName());
        studentDto.setStudentClass(user.getStudentClass());
        studentDto.setRole(user.getUserRole());
        studentDto.setFeeStatus(feestatus==null?false:feestatus);
        return studentDto;
    }

    @Override
    public ResponseEntity<?> applyLeave(StudentLeaveDto studentLeaveDto) {
        Optional<User>getuser = userRepositories.findById(studentLeaveDto.getUserId());
        if (getuser.isPresent()) {
            StudentLeave studentLeave=new StudentLeave();
            studentLeave.setBody(studentLeaveDto.getBody());
            studentLeave.setLeavefrom(studentLeaveDto.getLeavefrom());
            studentLeave.setLeaveto(studentLeaveDto.getLeaveto());
            studentLeave.setSubject(studentLeaveDto.getSubject());
            studentLeave.setStudentLeaveStatus(StudentLeaveStatus.Pending);
            studentLeave.setUser(getuser.get());
            this.studentLeaveRepositories.save(studentLeave);
            return ResponseEntity.ok(new MessageDto("Student leave approved Successfully",200, Math.toIntExact(studentLeaveDto.getUserId())));

        }
        return ResponseEntity.ok(new MessageDto("Student with this id not found!!",400, Math.toIntExact(studentLeaveDto.getId())));
    }

    @Override
    public ResponseEntity<?> triggerMail(MailTriggerDto mailTriggerDto) {
        MailSender.sendMail(mailTriggerDto);
        return new ResponseEntity<>(new MessageDto("Mail Sent Successfully",200,null), HttpStatus.OK);
    }


    @Override
    public void getFeeStatusPerStudent() {
        List<Object[]>getAlluser=this.feeRepository.getallStudentWithFee();
        if (!getAlluser.isEmpty()) {
            for (Object[] objects : getAlluser) {
                if (objects[2]==null || (Boolean) objects[2]==false){
                    MailTriggerDto triggerDto=new MailTriggerDto();
                    triggerDto.setBestregard("School Admin Team");
                    triggerDto.setName((String) objects[1]);
                    triggerDto.setSender("vishalpandey10022000@gmail.com");
                    triggerDto.setReplyTo("vishalpandey10022000@gmail.com");
                    triggerDto.setSubject("Fee Status");
                    triggerDto.setNotificationType("fee");
                    triggerDto.setReceiver((String) objects[0]);
                    triggerDto.setBody("Kindly Pay the fee of this month!!");
                    System.out.println(triggerDto);
                    MailSender.sendMail(triggerDto);
                }
            }
        }
    }

    @Override
    public void sendMailForStudentLeaveStatus() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plus(1, ChronoUnit.DAYS);
        List<Object[]>getAlluserleave=this.studentLeaveRepositories.getAllUserLeave(tomorrow.toLocalDate().atStartOfDay(),tomorrow.toLocalDate().atTime(23, 59, 59));
        if (!getAlluserleave.isEmpty()) {
            for (Object[] objects : getAlluserleave) {
                if (objects[2]!=null){
                    Timestamp leaveTo = (Timestamp) objects[2];
                    MailTriggerDto triggerDto=new MailTriggerDto();
                    triggerDto.setBestregard("School Admin Team");
                    triggerDto.setName((String) objects[1]);
                    triggerDto.setSender("vishalpandey10022000@gmail.com");
                    triggerDto.setReplyTo("vishalpandey10022000@gmail.com");
                    triggerDto.setSubject("Leave Reminder");
                    triggerDto.setNotificationType("leave");
                    triggerDto.setReceiver((String) objects[0]);
                    triggerDto.setBody("Your leave ends tomorrow on " + leaveTo.toLocalDateTime().toLocalDate() + ".");
                    System.out.println(triggerDto);
                    MailSender.sendMail(triggerDto);
                }
            }
        }
    }

    @Override
    public ResponseEntity<?> countAllStudents() {
        return new ResponseEntity<>(this.userRepositories.countAllStudents(),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> sendOtp(String email) {
        int otp = Integer.parseInt(new DecimalFormat("000000").format(new Random().nextInt(999999)));
        otpstore.put("otp",String.valueOf(otp));
        System.out.println(otpstore);
        MailTriggerDto triggerDto=new MailTriggerDto();
        triggerDto.setBestregard("School Admin Team");
        triggerDto.setSender("vishalpandey10022000@gmail.com");
        triggerDto.setReplyTo("vishalpandey10022000@gmail.com");
        triggerDto.setSubject("OTP");
        triggerDto.setNotificationType("otp");
        triggerDto.setReceiver(email);
        triggerDto.setName(this.userRepositories.findByEmail(email).get().getName());
        triggerDto.setBody("Your One-Time Password (OTP) for login is: <b>" + otp + "</b>. Please use this to proceed. The OTP is valid for 10 minutes.");
        MailSender.sendMail(triggerDto);
        return new ResponseEntity<>(new MessageDto("OTP Sent Successfully",200,otp),HttpStatus.OK);
    }

    @Override
    public Boolean validateOtp(String otp) {
        boolean validate=otpstore.get("otp").toString().equals(otp);
        otpstore.clear();
        return validate;
    }

    @Override
    public ResponseEntity<?> updatePassword(String email,String password) {
        Optional<User> user=this.userRepositories.findByEmail(email);
        if (user.isPresent()) {
            user.get().setPassword(new BCryptPasswordEncoder().encode(password));
            this.userRepositories.save(user.get());
            return ResponseEntity.ok(new MessageDto("Password Updated Successfully!!",200, null));
        }
        else{
            return ResponseEntity.ok(new MessageDto("Student with this mail not found!!",400, null));
        }
    }


}
