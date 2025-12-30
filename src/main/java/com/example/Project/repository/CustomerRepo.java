package com.example.Project.repository;

import com.example.Project.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer,Integer>{
    Customer findByUsername(String username);
}
