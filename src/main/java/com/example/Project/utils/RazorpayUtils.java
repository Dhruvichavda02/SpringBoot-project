package com.example.Project.utils;

import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RazorpayUtils {

    @Value("${razorpay.secret}")
    private String razorpaySecretKey;

    public boolean verifySignature(String orderId,String paymentId,String signature ){
        String payload = orderId + "|" + paymentId;

        String generatedSignature = HmacUtils
                .hmacSha256Hex(razorpaySecretKey,payload);

        return generatedSignature
                .equals(signature);
    }
}
