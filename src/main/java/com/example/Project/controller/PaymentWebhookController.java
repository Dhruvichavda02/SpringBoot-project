package com.example.Project.controller;

import com.example.Project.service.PaymentService;
import com.example.Project.service.PaymentVerifyService;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentWebhookController {

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;
        @Autowired
        private PaymentVerifyService paymentVerifyService;

        @PostMapping("/webhook")
        public ResponseEntity<String> handleWebhook(
                @RequestBody String payload,
                @RequestHeader("X-Razorpay-Signature") String signature
        ) {
            System.out.println(" WEBHOOK HIT ");
            try {
                // 🔐 Delegate everything to service
                paymentVerifyService.verifyAndProcessWebhook(payload, signature);

                // Razorpay only cares about 200 OK
                return ResponseEntity.ok("Webhook processed successfully");

            } catch (RazorpayException e) {
                e.printStackTrace();
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Webhook signature verification failed");

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Webhook processing error");
            }
        }
    }


