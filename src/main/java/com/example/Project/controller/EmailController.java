package com.example.Project.controller;

import com.example.Project.model.EmailDetails;
import com.example.Project.service.EmailServices.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/email-send")
public class EmailController {
    @Autowired
    private EmailService emailService;

    //send simple mail
    @PostMapping("/sendMail")
    public ResponseEntity sendMail(@RequestBody EmailDetails details){
        try{
            return new ResponseEntity(emailService.sendSimpleMail(details), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.OK);
        }
    }

    //send with attachment
    @PostMapping(value = "/mailWithAttachment", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity sendMailWithAttachment( @RequestParam String to, @RequestParam String subject, @RequestParam String message, @RequestParam MultipartFile file){
        try{
            return new ResponseEntity(emailService.sendMailWithAttachment(to,subject,message,file), HttpStatus.GONE);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.OK);
        }
    }
}
