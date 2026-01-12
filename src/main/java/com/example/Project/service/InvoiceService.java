package com.example.Project.service;

import com.example.Project.model.Payment;
import com.example.Project.model.customer.Customer;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;



import java.io.ByteArrayOutputStream;
import java.time.LocalDate;


@Service
public class InvoiceService {

    public byte[] generateInvoice(Payment payment, Customer customer) throws DocumentException {

        ByteArrayOutputStream out = new ByteArrayOutputStream(); //creates a buffer memory so that nothing is written in hard disk.PDF will be written into memory
        out.toByteArray(); // return the pdf content

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, out); // Write whatever goes into document ->out
            //document → out → byte[]

            document.open();
            document.add(new Paragraph("INVOICE"));
            document.add(new Paragraph("Inovice no: INV-" + payment.getId()));
            document.add(new Paragraph("Customer: " + payment.getId()));
            document.add(new Paragraph("Date: " + LocalDate.now()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Customer Name: " + customer.getName()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Razorpay Payment ID: " + payment.getRazorpayPaymentId()));
            document.add(new Paragraph("Amount Paid: ₹" + payment.getAmount()));

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice", e);
        }

        return out.toByteArray();
    }
}
