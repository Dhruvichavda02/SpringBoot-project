package com.example.Project.controller;

import com.example.Project.DTOs.CreatePaymentDTO;
import com.example.Project.DTOs.PaymentVerifyDTO;
import com.example.Project.service.PaymentService;
import com.example.Project.service.PaymentVerifyService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentVerifyService paymentVerifyService;

    //create
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentDTO dto){
        try{
            return new ResponseEntity(paymentService.createPaymentLink(dto), HttpStatus.OK);
        } catch (RazorpayException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.OK);
        }
    }



}
