package com.example.Project.controller;

import com.example.Project.service.PaymentService;
import com.example.Project.service.PaymentVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentWebhookController {

        @Autowired
        private PaymentVerifyService paymentVerifyService;

        @PostMapping("/webhook")
        public ResponseEntity<String> handleWebhook(
                @RequestBody String payload,
                @RequestHeader("X-Razorpay-Signature") String signature
        ) {
            System.out.println("🔥 WEBHOOK HIT 🔥");
            try {
                paymentVerifyService.verifyAndProcessWebhook(payload, signature);
                return ResponseEntity.ok("Webhook processed");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid webhook");
            }
        }
    }


