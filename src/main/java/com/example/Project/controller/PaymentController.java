package com.example.Project.controller;

import com.example.Project.DTOs.CreatePaymentDTO;
import com.example.Project.DTOs.PaymentVerifyDTO;
import com.example.Project.service.PaymentService;
import com.example.Project.service.PaymentVerifyService;
import com.razorpay.RazorpayException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SecurityRequirement(name="bearerAuth")
@RestController
@RequestMapping("/payment")
@Tag(name="Payment",description = "Payment link creation API")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    //create
    @PostMapping("/createLink")
    @Operation(summary = "Payment link creation")
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentDTO dto){
        try{
            return new ResponseEntity(paymentService.createPaymentLink(dto), HttpStatus.OK);
        } catch (RazorpayException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.OK);
        }
    }
}
