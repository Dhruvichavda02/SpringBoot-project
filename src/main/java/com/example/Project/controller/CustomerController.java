package com.example.Project.controller;


import com.example.Project.DTOs.LoginRequest;
import com.example.Project.DTOs.RegisterRequest;
import com.example.Project.model.customer.Customer;
import com.example.Project.service.AuthService;

import com.example.Project.service.CustomerAttachmentsService;
import com.example.Project.service.CustomerSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    AuthService authservice;
    @Autowired
    CustomerSevice customerSevice;
    @Autowired
    CustomerAttachmentsService attachmentsService;


    @PostMapping(value = "/register",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(@RequestBody RegisterRequest request) {
        try {
            return new ResponseEntity<>(authservice.register(request), HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request) {
        try {
            return new ResponseEntity(authservice.login(request),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.OK);
        }
    }

    @GetMapping("/all")
    public List<Customer> getAllCustomer(){
        return customerSevice.getAllCustomer();
    }


    @GetMapping("/{id}")
    public ResponseEntity getCustomerById(@PathVariable Integer id){
        return new ResponseEntity(customerSevice.getCustomerById(id),HttpStatus.OK);
    }

    //Update
    @PutMapping("/{id}")
    public  ResponseEntity update(@PathVariable Integer id, @RequestBody Customer customer){
        try{
            return new ResponseEntity(customerSevice.updateCustomer(customer,id) ,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.OK);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity DeactiveCustomer(@PathVariable Integer id){
        try{
            return new ResponseEntity(customerSevice.deactiveCustomer(id),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    //upload attachments
    @PostMapping(value = "{id}/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  ResponseEntity uploadAttachments(@PathVariable Integer id, @RequestParam("file")MultipartFile file){

        try{
            return new ResponseEntity(attachmentsService.uploadFile(id,file),HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST) ;
        }
    }

}
