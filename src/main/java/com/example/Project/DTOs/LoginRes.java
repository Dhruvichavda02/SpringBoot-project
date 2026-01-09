package com.example.Project.DTOs;

import com.example.Project.model.customer.Customer;

public class LoginRes {
    private Customer customer;
    private String token;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
