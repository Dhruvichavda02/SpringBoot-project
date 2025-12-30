package com.example.Project.service;


import com.example.Project.model.Customer;
import com.example.Project.model.CustomerPrincipal;
import com.example.Project.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyCustomerService implements UserDetailsService {
    @Autowired
    private CustomerRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer = repo.findByUsername(username);

        if(customer==null){
            System.out.println("Customer not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomerPrincipal(customer);
    }
}
