package com.school.dto;

import com.school.enums.StudentLeaveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveDto {
    private String name;
    private String email;
    private String body;
    private Date leavefrom;
    private Date leaveto;
    private StudentLeaveStatus leaveStatus;
    private String subject;
    private int userId;
}
