package com.example.Project.service.EmailServices;

import com.example.Project.model.EmailDetails;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendSimpleMail(EmailDetails details) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setSubject(details.getSubject());
            mailMessage.setText(details.getMsgBody());

            javaMailSender.send(mailMessage);
            //Converts message to SMTP format-->Connects to SMTP Server-->Authenticates using username/password-->Sends email

            return "Mail sent successfully";
        }catch (Exception e){
            return "Error while sending mail"+e.getMessage();
        }
    }

    @Override
    public String sendMailWithAttachment(String to,String sub,String message, MultipartFile file) {
        try{

            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage,true);

            helper.setTo(to);
            helper.setFrom(sender);
            helper.setSubject(sub);
            helper.setText(message);
            helper.addAttachment(file.getOriginalFilename(),file);

            javaMailSender.send(mimeMailMessage);
            return "Mail sent Successfully";
        } catch (Exception e) {
            return "Error while sending mail with attachment: " + e.getMessage();
        }
    }
}
