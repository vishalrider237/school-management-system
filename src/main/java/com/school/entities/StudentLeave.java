package com.school.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.school.enums.StudentLeaveStatus;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "STUDENT_LEAVE")
@Data
public class StudentLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    private String body;
    private Date leavefrom;
    private Date leaveto;
    private StudentLeaveStatus studentLeaveStatus;
    @ManyToOne(fetch = FetchType.LAZY,optional=false)
    @JoinColumn(name = "user_id",nullable = false)
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
