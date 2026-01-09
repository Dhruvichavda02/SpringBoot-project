package com.example.Project.repository;


import com.example.Project.model.customer.CustomerAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAttachmentsRepo extends JpaRepository<CustomerAttachment,Integer> {
    List<CustomerAttachment> findByCustomer_IdAndActiveTrue(Integer customerid);
}
