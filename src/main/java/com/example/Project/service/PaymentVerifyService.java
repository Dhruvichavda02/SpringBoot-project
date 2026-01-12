package com.example.Project.service;

import com.example.Project.enums.PaymentFor;
import com.example.Project.enums.PaymentStatus;
import com.example.Project.model.BookingModel;
import com.example.Project.model.OrderModel;
import com.example.Project.model.Payment;
import com.example.Project.model.customer.Customer;
import com.example.Project.repository.BookingRepo;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.repository.OrderRepository;
import com.example.Project.repository.PaymentRepository;
import com.example.Project.service.EmailServices.EmailService;
import com.itextpdf.text.DocumentException;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentVerifyService {


    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerRepository customerRepo;

    public void verifyAndProcessWebhook(String payload, String signature)
            throws RazorpayException, DocumentException {

        Utils.verifyWebhookSignature(payload, signature, webhookSecret);

        JSONObject json = new JSONObject(payload);

        String event = json.getString("event");

        //  ONLY handle payment_link.paid
        if (!"payment_link.paid".equals(event)) {
            return;
        }

        JSONObject paymentEntity = json
                .getJSONObject("payload")
                .getJSONObject("payment")
                .getJSONObject("entity");

        JSONObject paymentLinkEntity = json
                .getJSONObject("payload")
                .getJSONObject("payment_link")
                .getJSONObject("entity");

        String paymentId = paymentEntity.getString("id");           // pay_xxx
        String paymentLinkId = paymentLinkEntity.getString("id");   // plink_xxx
        Double amountpaid = paymentEntity.getInt("amount")/100.0;

        Payment payment = paymentRepository
                .findByRazorpayOrderId(paymentLinkId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        //Prevent duplicate processing
        if (payment.getStatus() == PaymentStatus.PAID) {
            return;
        }

        payment.setRazorpayPaymentId(paymentId);
        payment.setStatus(PaymentStatus.PAID);
        payment.setAmount(amountpaid);
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        Customer customer;
        if (payment.getPaymentFor() == PaymentFor.BOOKING) {
            BookingModel booking = bookingRepo
                    .findById(payment.getRefrence_id())
                    .orElseThrow();
            booking.setPaymentStatus(PaymentStatus.PAID);
            bookingRepo.save(booking);
            customer = customerRepo
                    .findById(booking.getCustId())
                    .orElseThrow();
        } else {
            OrderModel order = orderRepository
                    .findById(payment.getRefrence_id())
                    .orElseThrow();
            order.setPaymentStatus(PaymentStatus.PAID);
            orderRepository.save(order);

            customer = customerRepo
                    .findById(order.getCustomerId().getId())
                    .orElseThrow();

        }

        // invoice
        byte[] invoicePdf = invoiceService.generateInvoice(payment,customer);
        emailService.sendMailWithAttachment(customer,payment,invoicePdf);
    }
}
