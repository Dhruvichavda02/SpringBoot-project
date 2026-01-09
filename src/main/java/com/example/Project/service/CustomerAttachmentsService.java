package com.example.Project.service;


import com.example.Project.model.customer.Customer;
import com.example.Project.model.customer.CustomerAttachment;
import com.example.Project.repository.CustomerAttachmentsRepo;
import com.example.Project.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class CustomerAttachmentsService {

    private static final String Upload_dir =
            System.getProperty("user.dir") + "/uploads/customers/";


    @Autowired
    CustomerAttachmentsRepo customerAttachmentsRepo;
    @Autowired
    CustomerRepo customerRepo;

    public CustomerAttachment uploadFile(Integer id, MultipartFile file) throws IOException {
         // to check if the customer exist or not
        Customer customer =  customerRepo.findByIdAndActiveTrue(id).orElseThrow(()->new RuntimeException("Customer not found!"));

        //create folder if not exits
        File dir = new File(Upload_dir);
        if(!dir.exists()){
            dir.mkdir();
        }

        //Unique Filename
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filepath = Upload_dir + filename;

        //save file to disk
        file.transferTo(new File(filepath)); // Takes file from HTTP request and save to disk

        //save path to db
        CustomerAttachment attachment = new CustomerAttachment();
        attachment.setCustomer(customer);
        attachment.setPath(filepath);
        attachment.setActive(true);

    return customerAttachmentsRepo.save(attachment);


    }
}
