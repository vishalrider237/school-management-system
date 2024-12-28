package com.school.dto;

import com.school.enums.StudentLeaveStatus;
import lombok.Data;

import java.util.Date;

@Data
public class StudentLeaveDto {
    private Long id;
    private String subject;
    private String body;
    private Date leavefrom;
    private Date leaveto;
    private StudentLeaveStatus studentLeaveStatus;
    private Long userId;
}
