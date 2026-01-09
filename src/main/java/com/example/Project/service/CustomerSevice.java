package com.example.Project.service;


import com.example.Project.DTOs.CustomerAttachmentDTO;
import com.example.Project.DTOs.CustomerResponseDTO;
import com.example.Project.model.customer.Customer;
import com.example.Project.repository.CustomerAttachmentsRepo;
import com.example.Project.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerSevice {

    @Autowired
    private CustomerRepo repo;

    @Autowired
    private CustomerAttachmentsRepo attachmentsRepo;

        public List<Customer> getAllCustomer(){
           return repo.findByActiveTrue();
        }

        //update
        public Customer updateCustomer(Customer updatedCustomer,Integer id){

            Customer existingCustomer = repo.findByIdAndActiveTrue(id).orElseThrow(() -> new RuntimeException("Customer not found"));
            if (updatedCustomer.getContact() != null &&
                    !updatedCustomer.getContact().equals(existingCustomer.getContact())) {

                throw new RuntimeException("Contact number cannot be updated");
            }
            existingCustomer.setName(updatedCustomer.getName());
            existingCustomer.setAddress(updatedCustomer.getAddress());


            return repo.save(existingCustomer);

        }

        //soft-delete
       public String deactiveCustomer(Integer id){
            Customer customer = repo.findByIdAndActiveTrue(id).orElseThrow(()-> new RuntimeException("User does not exist!"));

            customer.setActive(false);
            repo.save(customer);
           return "Customer deactivated successfully";
        }


        //getCustomerById
        public CustomerResponseDTO getCustomerById(Integer id){

            Customer customer = repo.findByIdAndActiveTrue(id).orElseThrow(()->new RuntimeException("Customer not found!"));

            List<CustomerAttachmentDTO> attachment = attachmentsRepo.findByCustomer_IdAndActiveTrue(id).
                    stream()
                    .map(att-> {
                        CustomerAttachmentDTO dto = new CustomerAttachmentDTO();
                        dto.setId(att.getId());
                        dto.setPath(att.getPath());
                        dto.setActive(att.getActive());
                        return dto;
                    }).toList();

            CustomerResponseDTO response= new CustomerResponseDTO();
            response.setId(customer.getId());
            response.setName(customer.getName());
            response.setAttachmentDTOS(attachment);
            response.setAddress(customer.getAddress());
            response.setContact(customer.getContact());
            response.setActive(customer.getActive());

            return response;
        }



}
