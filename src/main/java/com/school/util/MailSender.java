package com.school.util;

import com.school.dto.MailTriggerDto;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {
    public static void sendMail(MailTriggerDto mailTriggerDto){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        String authenticatedEmail = AppConstant.from_mailID;  // Fixed sender
        String authenticatedPassword = AppConstant.from_mail_password;
        String receiverEmail = mailTriggerDto.getReceiver();
        String dynamicSender = mailTriggerDto.getSender();
        String replyToEmail = mailTriggerDto.getReplyTo();

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(authenticatedEmail, authenticatedPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(authenticatedEmail));
            message.setReplyTo(InternetAddress.parse(replyToEmail.trim()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail.trim()));
            if (dynamicSender != null && !dynamicSender.isEmpty()) {
                message.setFrom(new InternetAddress(dynamicSender));  // Spoof "From"
            }
            message.setSubject(getEmailSubject(mailTriggerDto.getSubject()));
            message.setContent(getEmailBody(mailTriggerDto.getNotificationType(), mailTriggerDto.getName(), mailTriggerDto.getBody(),mailTriggerDto.getBestregard()),
                    "text/html; charset=utf-8");
            Transport.send(message);
            System.out.println("Email Sent Successfully from: " + dynamicSender + " as " + receiverEmail);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    private static String getEmailSubject(String subject) {
        return subject.replace("for", "");
    }

    public static String getEmailBody(String notificationType, String name, String body,String bestregard) {
        String emailTitle = "Notification";
        String header = "<h1>Notification</h1>";
        String footer = "<p>Thank you!<br/>Best regards,<br/>"+bestregard+"</p>";

        switch (notificationType.toLowerCase()) {
            case "leave":
                emailTitle = "Leave Approval Notification";
                header = "<h1>Leave Request Notification</h1>";
                break;
            case "fee":
                emailTitle = "Fee Payment Notification";
                header = "<h1>Fee Payment Details</h1>";
                break;
            case "otp":
                emailTitle = "Your OTP Code";
                header = "<h1 style='color:#3498db;'>Your OTP for Verification</h1>";
                break;
            default:
                header = "<h1>General Notification</h1>";
        }

        return String.format(
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; line-height: 1.6; }" +
                        "h1 { color: #333366; }" +
                        "p { font-size: 14px; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "%s" +  // Dynamic Header
                        "<p>Dear %s,</p>" +
                        "<p>%s</p>" +
                        "%s" +  // Footer
                        "</body></html>",
                header, name, body, footer);
    }
}
