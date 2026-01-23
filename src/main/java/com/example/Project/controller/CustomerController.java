package com.example.Project.controller;


import com.example.Project.DTOs.LoginRequest;
import com.example.Project.DTOs.RegisterRequest;
import com.example.Project.model.customer.Customer;
import com.example.Project.service.AuthService;

import com.example.Project.service.CustomerAttachmentsService;
import com.example.Project.service.CustomerSevice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@SecurityRequirement(name="bearerAuth")
@RestController
@RequestMapping("/customer")
@Tag(name ="Customer",description = "Customer Management API")
public class CustomerController {

    @Autowired
    private AuthService authService;
    @Autowired
    private CustomerSevice customerSevice;
    @Autowired
    private CustomerAttachmentsService attachmentsService;


    @PostMapping(value = "/register",consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Customer registration ")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // take first validation error
            String errorMessage = bindingResult
                    .getFieldErrors()
                    .get(0)
                    .getDefaultMessage();

            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        try {
            return new ResponseEntity<>(
                    authService.register(request),
                    HttpStatus.CREATED
            );
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(
                    ex.getMessage(),
                    HttpStatus.CONFLICT
            );
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Customer login ")
    public ResponseEntity login(@RequestBody LoginRequest request) {
        try {
            return new ResponseEntity(authService.login(request),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.OK);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all customer")
    public List<Customer> getAllCustomer(){
        return customerSevice.getAllCustomer();
    }


    @GetMapping("/getById/{id}")
    @Operation(summary = "Get Customer by Id")
    public ResponseEntity getCustomerById(@PathVariable Integer id){
        return new ResponseEntity(customerSevice.getCustomerById(id),HttpStatus.OK);
    }

    //Update
    @PutMapping("/update/{id}")
    @Operation(summary = "Customer update")
    public  ResponseEntity update(@PathVariable Integer id, @RequestBody Customer customer){
        try{
            return new ResponseEntity(customerSevice.updateCustomer(customer,id) ,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deactive customer")
    public ResponseEntity DeactiveCustomer(@PathVariable Integer id){
        try{
            return new ResponseEntity(customerSevice.deactiveCustomer(id),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    //upload attachments
    @PostMapping(value = "{id}/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Customer attchments upload")
    public  ResponseEntity uploadAttachments(@PathVariable Integer id, @RequestParam("file")MultipartFile file){

        try{
            return new ResponseEntity(attachmentsService.uploadFile(id,file),HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST) ;
        }
    }

}
