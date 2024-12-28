package com.school.controllers;

import com.school.dto.FeeDto;
import com.school.dto.MailTriggerDto;
import com.school.dto.StudentDto;
import com.school.dto.StudentLeaveDto;
import com.school.entities.StudentLeave;
import com.school.services.sudent.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        return studentService.getStudentbyId(id);
    }
    @PostMapping("/leave")
    public ResponseEntity<?> applyLeave(@RequestBody StudentLeaveDto studentLeaveDto) {
        return studentService.applyLeave(studentLeaveDto);
    }
    @PostMapping("/sendMail")
    public ResponseEntity<?> triggerMailForLeaveUpdate(@RequestBody MailTriggerDto mailTriggerDto) {
        return studentService.triggerMail(mailTriggerDto);
    }
    @GetMapping("/countStudents")
    public ResponseEntity<?> countStudents() {
        return studentService.countAllStudents();
    }
    @GetMapping("/sendOtp/{email}")
    public ResponseEntity<?> sendOtp(@PathVariable String email) {
        return studentService.sendOtp(email);
    }
    @GetMapping("/otpvalidate/{otp}")
    public Boolean otpvalidate(@PathVariable String otp) {
        return studentService.validateOtp(otp);
    }
    @PutMapping("updatePassowrd/{email}/{password}")
    public ResponseEntity<?> updatePassword(@PathVariable String email,@PathVariable String password) {
        return studentService.updatePassword(email,password);
    }
}
