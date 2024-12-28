package com.school.services.Admin;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.school.dto.*;
import com.school.entities.Fee;
import com.school.entities.StudentLeave;
import com.school.entities.User;
import com.school.enums.StudentLeaveStatus;
import com.school.enums.UserRole;
import com.school.repositories.FeeRepository;
import com.school.repositories.StudentLeaveRepositories;
import com.school.repositories.UserRepositories;
import com.school.util.MailSender;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    private UserRepositories userRepositories;

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StudentLeaveRepositories studentLeaveRepositories;

    @PostConstruct //this method is called when application run first time,no api is required to call this service
    public void createAdminAccount(){
        List<User> adminuser=this.userRepositories.findByUserRole(UserRole.ADMIN);
        if(adminuser.isEmpty()){
            User adminaccount = new User();
            adminaccount.setEmail("vishalpandey10022000@gmail.com");
            adminaccount.setName("Vishal Pandey");
            adminaccount.setUserRole(UserRole.ADMIN);
            adminaccount.setPassword(new BCryptPasswordEncoder().encode("admin"));
            adminaccount.setAddress("Raghunathpur,Bihar");
            adminaccount.setDateOfBirth(java.sql.Date.valueOf("2000-02-10"));
            adminaccount.setGender("Male");
            adminaccount.setFatherName("Manoj Pandey");
            adminaccount.setMotherName("Meena Pandey");
            adminaccount.setCreatedOn(new Timestamp(System.currentTimeMillis()));
            adminaccount.setStudentClass("Software Engineer");
            userRepositories.save(adminaccount);
        }

    }

    @Override
    public ResponseEntity<?> addStudent(StudentDto student) {
      Optional<User> userexist=this.userRepositories.findByEmail(student.getEmail());
      if (userexist.isPresent()) {
          return ResponseEntity.ok(new MessageDto("Student with this email already exist!!",400, null));
      }
        User user=new User();
        try {
            user=modelMapper.map(student,User.class);
            user.setPassword(new BCryptPasswordEncoder().encode(student.getPassword()));
            user.setUserRole(UserRole.STUDENT);
            user.setCreatedOn(new Timestamp(System.currentTimeMillis()));
            this.userRepositories.save(user);

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(new MessageDto("Student created Successfully",200, (int) user.getId()));
    }

    @Override
    public ResponseEntity<?> getAllStudents(int page,int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> studentPage = userRepositories.findByUserRole(UserRole.STUDENT, pageable);
        Page<StudentDto> studentDTOPage = studentPage.map(this::mapToDTO);
        return new ResponseEntity<>(studentDTOPage, HttpStatus.OK);
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
    public ResponseEntity<?> deleteStudent(Long id) {
        Optional<User> userexist=this.userRepositories.findById(id);
        if (userexist.isPresent()) {
            this.userRepositories.delete(userexist.get());
        }
        else{
            return ResponseEntity.ok(new MessageDto("Student with this id not found!!",400, Math.toIntExact(id)));
        }
        return ResponseEntity.ok(new MessageDto("Student deleted Successfully",200, Math.toIntExact(id)));

    }

    @Override
    public ResponseEntity<?> getStudentbyId(Long id) {
        return ResponseEntity.ok(this.userRepositories.findById(id).get());

    }

    @Override
    public ResponseEntity<?> updateStudent(StudentDto student) {
        Optional<User> userexist=this.userRepositories.findById(student.getId());
        if (userexist.isPresent()) {
            User user=userexist.get();
            user.setEmail(student.getEmail());
            user.setPassword(new BCryptPasswordEncoder().encode(student.getPassword()));
            user.setName(student.getName());
            user.setGender(student.getGender());
            user.setAddress(student.getAddress());
            user.setDateOfBirth(student.getDateOfBirth());
            user.setFatherName(student.getFatherName());
            user.setMotherName(student.getMotherName());
            user.setStudentClass(student.getStudentClass());
            user.setCreatedOn(new Timestamp(System.currentTimeMillis()));
            userRepositories.save(user);
        }
        else{
            return ResponseEntity.ok(new MessageDto("Student with this id not found!!",400, Math.toIntExact(student.getId())));
        }
        return ResponseEntity.ok(new MessageDto("Student updated Successfully",200, Math.toIntExact(student.getId())));

    }

    @Override
    public ResponseEntity<?> postFee(Long studentId,FeeDto fee) {
        Optional<User>optionalUser=this.userRepositories.findById(studentId);
        if (optionalUser.isPresent()) {
            Fee fee1=new Fee();
            fee1.setAmount(fee.getAmount());
            fee1.setMonth(fee.getMonth());
            fee1.setDescription(fee.getDescription());
            fee1.setCreatedDate(fee.getCreatedDate());
            fee1.setGivenBy(fee.getGivenBy());
            fee1.setUser(optionalUser.get());
            fee1.setFeeStatus(true);
            feeRepository.save(fee1);
            MailTriggerDto triggerDto=new MailTriggerDto();
            triggerDto.setSubject("Student Fee Status");
            triggerDto.setBody(optionalUser.get().getName()+" has paid fee Successfully!!");
            triggerDto.setReceiver("vishalpandey10022000@gmail.com");
            triggerDto.setNotificationType("fee");
            triggerDto.setName("Vishal Pandey");
            triggerDto.setBestregard("School Admin Team");
            triggerDto.setSender(optionalUser.get().getEmail());
            triggerDto.setReplyTo(optionalUser.get().getEmail());
            MailSender.sendMail(triggerDto);
            return ResponseEntity.ok(new MessageDto("Fee Paid Successfully",200, studentId.intValue()));
        }
        return ResponseEntity.ok(new MessageDto("Internal Server Error",500, studentId.intValue()));
    }

    @Override
    public ResponseEntity<?> findAllLeaves() {
        List<Object>getAllLeaves=this.studentLeaveRepositories.findAllLeaves();
        List<LeaveDto> leaveDTOList = new ArrayList<>();
        for (Object obj : getAllLeaves) {
            Object[] row = (Object[]) obj;
            String name = (String) row[0];
            String email = (String) row[1];
            String body = (String) row[2];
            java.sql.Timestamp timestamp = (java.sql.Timestamp) row[3];
            Date leavefrom = timestamp != null ? new Date(timestamp.getTime()) : null;
            java.sql.Timestamp timestamp2 = (java.sql.Timestamp) row[4];
            Date leaveto = timestamp2 != null ? new Date(timestamp2.getTime()) : null;
            int statusIndex = (Integer) row[5];
            StudentLeaveStatus leaveStatus = StudentLeaveStatus.values()[statusIndex];
            String subject = (String) row[6];
            Object userIdObj = row[7];
            int userId = (userIdObj instanceof BigInteger) ? ((BigInteger) userIdObj).intValue() : (Integer) userIdObj;
            // Create and add the DTO to the list
            LeaveDto leaveDTO = new LeaveDto(name, email, body, leavefrom,leaveto, leaveStatus, subject,userId);
            leaveDTOList.add(leaveDTO);
        }
       return new ResponseEntity<>(leaveDTOList,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateStatus(Long userId,Integer status) {
        List<StudentLeave>getLeaves=this.studentLeaveRepositories.findByUserId(userId);
        if (getLeaves.isEmpty()){
            return ResponseEntity.ok(new MessageDto("No leaves are applied for this user!!",404, Math.toIntExact(userId)));
        }
        getLeaves.forEach(StudentLeave->{
            StudentLeave.setStudentLeaveStatus(StudentLeaveStatus.values()[status]);
            this.studentLeaveRepositories.save(StudentLeave);
        });
        return ResponseEntity.ok(new MessageDto("Leave Approved!!",200, Math.toIntExact(userId)));
    }

    @Override
    public ResponseEntity<?> triggerMail(MailTriggerDto mailTriggerDto) {
        MailSender.sendMail(mailTriggerDto);
     return new ResponseEntity<>(new MessageDto("Mail Sent Successfully",200,null),HttpStatus.OK);
    }

    @Override
    public byte[] exportToExcel() throws IOException {
        List<Object[]>getAllRecords=this.userRepositories.getAllRecord();
        if (!getAllRecords.isEmpty()){
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("User Data");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Name", "Email", "Student Class", "Address", "Date Of Birth","Father Name","Gender",
            "Mother's Name","Leave Body","Leave From","Leave To","Subject","Amount","Description","Paid By","Month","Fee Status"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            int rowIdx = 1;
            for (Object[] row : getAllRecords) {
                Row excelRow = sheet.createRow(rowIdx++);
                for (int col = 0; col < row.length; col++) {
                    excelRow.createCell(col).setCellValue(row[col] != null ? row[col].toString() : "");
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();
            return out.toByteArray();
        }
        return null;
    }

    @Override
    public byte[] exportToPdf() throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // Title
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("User Data Report", font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Table Setup
        String[] headers = {
                "Name", "Email", "Student Class", "Address", "Date Of Birth", "Father Name", "Gender",
                "Mother's Name", "Leave Body", "Leave From", "Leave To", "Subject", "Amount", "Description",
                "Paid By", "Month", "Fee Status"
        };

        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);  // Use full width of the page


        float[] columnWidths = {10, 12, 8, 20, 8, 8, 6, 8, 16, 8, 8, 9, 7, 16, 8, 7, 7};
        table.setWidths(columnWidths);

        // Add Header Row
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        // Add Data Rows
        List<Object[]> getAllRecords = this.userRepositories.getAllRecord();
        for (Object[] row : getAllRecords) {
            for (Object cell : row) {
                PdfPCell pdfCell = new PdfPCell(new Phrase(cell != null ? cell.toString() : ""));
                pdfCell.setPadding(5);
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);  // Align data to the left
                table.addCell(pdfCell);
            }
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }

}


