package com.example.Project.DTOs;

import com.example.Project.enums.PaymentFor;
import com.example.Project.enums.PaymentStatus;

public class CreatePaymentDTO {
    private Integer referenceId;
    private PaymentFor paymentFor;



    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public PaymentFor getPaymentFor() {
        return paymentFor;
    }

    public void setPaymentFor(PaymentFor paymentFor) {
        this.paymentFor = paymentFor;
    }

}
