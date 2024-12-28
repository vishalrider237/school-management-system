package com.school.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FEE")
@Data
public class Fee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String month;
    private String givenBy;
    private Long amount;
    private String description;
    private Date createdDate;
    private Boolean feeStatus;
    @ManyToOne(fetch = FetchType.LAZY,optional=false)
    @JoinColumn(name = "user_id",nullable = false)
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
