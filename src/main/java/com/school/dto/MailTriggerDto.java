package com.school.dto;

import lombok.Data;

@Data
public class MailTriggerDto {
    private String name;
    private String subject;
    private String body;
    private String receiver;
    private String sender;
    private String replyTo;
    private String NotificationType;
    private String bestregard;
}
