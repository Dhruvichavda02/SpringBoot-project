package com.example.Project.service;


import com.example.Project.model.Customer;
import com.example.Project.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerSevice {

    @Autowired
    private CustomerRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public Customer register(Customer customer) {
        customer.setpassword(encoder.encode(customer.getpassword()));
        repo.save(customer);
        return customer;
    }

    public Customer getByUsername(String username) {
        return repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}
