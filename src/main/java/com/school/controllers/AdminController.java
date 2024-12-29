package com.school.controllers;

import com.school.dto.FeeDto;
import com.school.dto.MailTriggerDto;
import com.school.dto.StudentDto;
import com.school.dto.StudentSearchDto;
import com.school.services.Admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/create")
    public ResponseEntity<?> createStudent(@RequestBody StudentDto student) {
        return adminService.addStudent(student);
    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllStudents( @RequestParam(defaultValue = "0")int page,  @RequestParam(defaultValue = "10")int size) {
        return adminService.getAllStudents(page,size);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        return adminService.getStudentbyId(id);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        return adminService.deleteStudent(id);
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateStudent(@RequestBody StudentDto student) {
        return adminService.updateStudent(student);
    }
    @PostMapping("/create/{studentId}")
    public ResponseEntity<?> postFee(@PathVariable Long studentId,@RequestBody FeeDto fee) {
        return adminService.postFee(studentId,fee);
    }
    @GetMapping("/leave/getAll")
    public ResponseEntity<?> findAllLeaves() {
        return adminService.findAllLeaves();
    }
    @PutMapping("/update/{userId}/{status}")
    public ResponseEntity<?> updateLeaveStatus(@PathVariable Long userId,@PathVariable Integer status) {
        return adminService.updateStatus(userId,status);
    }
    @PostMapping("/sendMail")
    public ResponseEntity<?> triggerMailForLeaveUpdate(@RequestBody MailTriggerDto mailTriggerDto) {
        return adminService.triggerMail(mailTriggerDto);
    }
    @GetMapping("exportToExcel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        byte[] excelFile =adminService.exportToExcel();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_data.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }
    @GetMapping("exportToPdf")
    public ResponseEntity<byte[]> exportToPdf() throws Exception {
        byte[] pdf = adminService.exportToPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_data.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    @PostMapping("/search")
    public ResponseEntity<?> searchStudent(@RequestBody StudentSearchDto searchDto) {
        return adminService.searchStudent(searchDto.getName(),searchDto.getMail());
    }
}
