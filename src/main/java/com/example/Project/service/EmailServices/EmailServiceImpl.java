package com.example.Project.service.EmailServices;

import com.example.Project.model.EmailDetails;
import com.example.Project.model.Payment;
import com.example.Project.model.customer.Customer;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender; // built in emailSender

//    @Override
//    public String sendSimpleMail(EmailDetails details) {
//        try{
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//            mailMessage.setFrom(sender);
//            mailMessage.setTo(details.getRecipient());
//            mailMessage.setSubject(details.getSubject());
//            mailMessage.setText(details.getMsgBody());
//
//            javaMailSender.send(mailMessage);
//            //Converts message to SMTP format-->Connects to SMTP Server-->Authenticates using username/password-->Sends email
//
//            return "Mail sent successfully";
//        }catch (Exception e){
//            return "Error while sending mail"+e.getMessage();
//        }
//    }

    @Override
    public String sendMailWithAttachment(Customer customer, Payment payment, byte[] invoicePdf) {
        try{

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);

            helper.setTo(payment.getEmail());
            helper.setSubject("Payment Successfull");
            helper.setText("""
                        Dear Customer,
                    
                                            Thank you for your payment.
                    
                                            Payment ID: %s
                                            Amount Paid: ₹%.2f
                    
                                            Please find your invoice attached.
                    """.formatted(
                            payment.getRazorpayPaymentId(),
                            payment.getAmount()
            ));

            helper.addAttachment("invoice.pdf",new ByteArrayResource(invoicePdf));
            javaMailSender.send(message);

            return "Mail sent Successfully";
        } catch (Exception e) {
            return "Error while sending mail with attachment: " + e.getMessage();
        }
    }
}
