package com.example.Project.repository;

import com.example.Project.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    Customer findByContactAndActiveTrue(String contact);
    List<Customer> findByActiveTrue();

    Optional<Customer> findByIdAndActiveTrue(Integer id);

}
