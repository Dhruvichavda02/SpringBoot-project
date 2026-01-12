package com.example.Project.service.EmailServices;

import com.example.Project.model.EmailDetails;
import com.example.Project.model.Payment;
import com.example.Project.model.customer.Customer;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService  {

  //  String sendSimpleMail(EmailDetails details);
    String sendMailWithAttachment(Customer customer, Payment payment,byte[] invoicePdf);
}
