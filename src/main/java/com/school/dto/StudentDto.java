package com.school.dto;

import com.school.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String fatherName;
    private String motherName;
    private String StudentClass;
    private Date dateOfBirth;
    private String address;
    private String gender;
    private UserRole role;
    private Boolean feeStatus;

}
