package com.example.Project.Service;

import com.example.Project.model.Payment;
import com.example.Project.model.customer.Customer;
import com.example.Project.service.InvoiceService;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @InjectMocks
    private InvoiceService invoiceService;

    private Payment payment;
    private Customer customer;

    @BeforeEach
    void setUp() {
        invoiceService = new InvoiceService();

        payment = new Payment();
        payment.setId(1);
        payment.setAmount(1500.0);
        payment.setRazorpayPaymentId("pay_123456");

        customer = new Customer();
        customer.setName("Joey Doe");

    }
    @Test
    void generateInvoice_success() throws DocumentException {

        byte[] pdfBytes = invoiceService.generateInvoice(payment, customer);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }
}
