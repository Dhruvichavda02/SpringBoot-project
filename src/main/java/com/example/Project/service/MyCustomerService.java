package com.example.Project.service;


import com.example.Project.model.UserMST;
import com.example.Project.repository.UserMstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyCustomerService implements UserDetailsService {
    @Autowired
    private UserMstRepository userMstRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            UserMST user = userMstRepository.findByUsername(username) .orElseThrow(() ->
                    new UsernameNotFoundException("User not found"));

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities("USER")
                    .build();


    }
}
