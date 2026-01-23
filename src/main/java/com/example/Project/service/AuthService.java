package com.example.Project.service;


import com.example.Project.DTOs.LoginRequest;
import com.example.Project.DTOs.LoginRes;
import com.example.Project.DTOs.RegisterRequest;
import com.example.Project.model.customer.Customer;
import com.example.Project.model.UserMST;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.repository.UserMstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserMstRepository userRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JwtServices jwtServices;

    @Autowired
    private PasswordEncoder encoder;

    public Customer register(RegisterRequest requets){

        if (customerRepo.existsByContact(requets.getContact())) {
            throw new RuntimeException("Contact number already registered");
        }
            UserMST userMST = new UserMST();
            userMST.setUsername(requets.getContact());
            userMST.setPassword(encoder.encode(requets.getPassword()));
            UserMST save = userRepo.saveAndFlush(userMST);

            Customer customer = new Customer();
            customer.setName(requets.getName());
            customer.setContact(requets.getContact());
            customer.setAddress(requets.getAddress());
            customer.setUser(save);
            return customerRepo.save(customer);

    }

    public LoginRes login(LoginRequest request) {
        Customer customer = customerRepo.findByContactAndActiveTrue(request.getContact());
        if(customer==null){
            throw new RuntimeException("Customer Not found with given username.");
        }
        Optional<UserMST> byUsername = userRepo.findByUsername(request.getContact());
        if(encoder.matches(request.getPassword(),byUsername.get().getPassword()))
        {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getContact(),
                            request.getPassword()
                    )
            );
            LoginRes loginRes = new LoginRes();
            loginRes.setCustomer(customer);
            loginRes.setToken(jwtServices.generateToken(request.getContact()));
            return loginRes;
        }else{
            throw new RuntimeException("Your password is incorrect.");
        }
    }




}
