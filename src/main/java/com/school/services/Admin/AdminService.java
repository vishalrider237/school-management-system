package com.school.services.Admin;

import com.itextpdf.text.DocumentException;
import com.school.dto.FeeDto;
import com.school.dto.MailTriggerDto;
import com.school.dto.StudentDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AdminService {
    ResponseEntity<?> addStudent(StudentDto student);

    ResponseEntity<?> getAllStudents(int page,int size);

    ResponseEntity<?> deleteStudent(Long id);

    ResponseEntity<?> getStudentbyId(Long id);

    ResponseEntity<?> updateStudent(StudentDto student);

    ResponseEntity<?> postFee(Long studentId,FeeDto fee);

    ResponseEntity<?> findAllLeaves();

    ResponseEntity<?> updateStatus(Long userid,Integer status);

    ResponseEntity<?> triggerMail(MailTriggerDto mailTriggerDto);

    byte[] exportToExcel() throws IOException;

    byte[] exportToPdf() throws DocumentException;
}
