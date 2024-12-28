package com.school.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FeeDto {
    private long id;
    private String month;
    private String givenBy;
    private Long amount;
    private String description;
    private Date createdDate;
}
