package com.school.entities;

import com.school.enums.UserRole;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "USERS")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;
    private String fatherName;
    private String motherName;
    private String StudentClass;
    private Date dateOfBirth;
    private String address;
    private String gender;
    private Timestamp createdOn;
}
