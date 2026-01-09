package com.example.Project.service.EmailServices;

import com.example.Project.model.EmailDetails;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService  {

    String sendSimpleMail(EmailDetails details);
    String sendMailWithAttachment(String to,String sub,String message, MultipartFile file);
}
