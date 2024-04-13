package com.tuan.springbootfinal.service;

import com.tuan.springbootfinal.entity.EmailDetails;

public interface EmailService {
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);


}
