package com.example.Project.service;

import com.example.Project.DTOs.CreatePaymentDTO;
import com.example.Project.enums.PaymentFor;
import com.example.Project.enums.PaymentStatus;
import com.example.Project.model.BookingModel;
import com.example.Project.model.OrderModel;
import com.example.Project.model.Payment;
import com.example.Project.repository.BookingRepo;
import com.example.Project.repository.OrderRepository;
import com.example.Project.repository.PaymentRepository;
import com.example.Project.utils.RazorpayUtils;
import com.razorpay.Order;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private OrderRepository orderRepository;



    public Map<String,Object> createPaymentLink(CreatePaymentDTO dto) throws RazorpayException {
        Double amount;



        if(dto.getPaymentFor() == PaymentFor.BOOKING){

            BookingModel booking = bookingRepo.findById(dto.getReferenceId())
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            amount = booking.getAmount();

        } else if (dto.getPaymentFor() == PaymentFor.ORDER) {
            OrderModel order = orderRepository.findActiveOrdersById(dto.getReferenceId())
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            amount = order.getAmount();

        }else{
            throw new RuntimeException("Invalid Payment type");
        }

        JSONObject option = new JSONObject();
        option.put("amount",amount*100);
        option.put("currency","INR");

        System.out.println("Razorpay Request Payload:");
        System.out.println(option.toString());

            PaymentLink link = razorpayClient.paymentLink.create(option);
            //save the payment
            Payment payment = new Payment();
            payment.setRefrence_id(dto.getReferenceId());
            payment.setPaymentFor(dto.getPaymentFor());
            payment.setAmount(amount);
            payment.setRazorpayOrderId(link.get("id"));
            payment.setStatus(PaymentStatus.CREATED);
            payment.setCreatedAt(LocalDateTime.now());
            payment.setEmail(dto.getEmail());

            paymentRepository.save(payment);

            return Map.of(
                    "payment_link" , link.get("short_url"),
                    "payment_link_id", link.get("id")
            );
    }


}
