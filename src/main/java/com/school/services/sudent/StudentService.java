package com.school.services.sudent;

import com.school.dto.FeeDto;
import com.school.dto.MailTriggerDto;
import com.school.dto.StudentLeaveDto;
import org.springframework.http.ResponseEntity;

public interface StudentService {
    ResponseEntity<?> getStudentbyId(Long id);

    ResponseEntity<?> applyLeave(StudentLeaveDto studentLeaveDto);

    ResponseEntity<?> triggerMail(MailTriggerDto mailTriggerDto);


    void getFeeStatusPerStudent();

    void sendMailForStudentLeaveStatus();

    ResponseEntity<?> countAllStudents();

    ResponseEntity<?> sendOtp(String email);

    Boolean validateOtp(String otp);

    ResponseEntity<?> updatePassword(String email,String password);
}
