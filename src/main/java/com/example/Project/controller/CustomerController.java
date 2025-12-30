package com.example.Project.controller;


import com.example.Project.model.Customer;
import com.example.Project.service.CustomerSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerSevice service;

    @Autowired
    AuthenticationManager authManager;

    // register
    @PostMapping
    public Customer register(@RequestBody Customer customer){
        return service.register(customer);
    }

    // login
    @PostMapping
    public Customer login(@RequestBody Customer cust){
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken( cust.getUsername(),cust.getpassword()));

    if(authentication.isAuthenticated())
    {
        return service.getByUsername(cust.getUsername());
    }
    return  null;
    }

}
