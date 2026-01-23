package com.example.Project.Service;

import com.example.Project.DTOs.CustomerResponseDTO;
import com.example.Project.model.customer.Customer;
import com.example.Project.model.customer.CustomerAttachment;
import com.example.Project.repository.CustomerAttachmentsRepo;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.service.CustomerSevice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerAttachmentsRepo customerAttachmentsRepo;

    @InjectMocks
    private CustomerSevice customerSevice;


    @Test
    void getAllCustomer(){
        Customer customer = new Customer();
        customer.setActive(true);
        customer.setId(1);

        when(customerRepository.findByActiveTrue()).thenReturn(List.of(customer));

        List<Customer> res = customerSevice.getAllCustomer();
        assertEquals(1,res.size());
        verify(customerRepository, times(1)).findByActiveTrue();
    }
    @Test
    void update_Success(){
        Customer customer = new Customer();
        customer.setId(1);
        customer.setContact("99999999999");
        customer.setName("Joe Doe");
        customer.setAddress("XYZ");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1);
        updatedCustomer.setContact("99999999999");
        updatedCustomer.setName("Joey Tribbiani");
        updatedCustomer.setAddress("XYZ");

        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer res = customerSevice.updateCustomer(updatedCustomer,1);

        assertEquals("Joey Tribbiani",res.getName());
    }

    @Test
    void update_contactNotAllowed(){
        Customer existing = new Customer();
        existing.setId(1);
        existing.setContact("9999999999");
        existing.setActive(true);

        Customer updateCustomer = new Customer();
        updateCustomer.setContact("8888888888");

        when(customerRepository.findByIdAndActiveTrue(1))
                .thenReturn(Optional.of(existing));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                ()-> customerSevice.updateCustomer(updateCustomer,1)
        );

        assertEquals("Contact number cannot be updated",ex.getMessage());
    }

    @Test
    void deactivated_success(){
        Customer existing = new Customer();
        existing.setId(1);
        existing.setActive(true);

        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(existing));

        assertEquals("Customer deactivated successfully",customerSevice.deactiveCustomer(1));
        assertFalse(existing.getActive());
        verify(customerRepository).save(existing);
    }

    @Test
    void deactivated_userNotFound(){
        Integer customerId = 1;

        when(customerRepository.findByIdAndActiveTrue(customerId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                ()->customerSevice.deactiveCustomer(customerId)
        );

        assertEquals("User does not exist!",ex.getMessage());
        verify(customerRepository,never()).save(any(Customer.class));
    }

    @Test
    void getCustomerById_success(){
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("John");
        customer.setContact("9999999999");
        customer.setAddress("Delhi");
        customer.setActive(true);

        CustomerAttachment attachment = new CustomerAttachment();
        attachment.setPath("/aadhar.pdf");
        attachment.setActive(true);
        attachment.setId(1);

        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(customer));
        when(customerAttachmentsRepo.findByCustomer_IdAndActiveTrue(1)).thenReturn(List.of(attachment));

        CustomerResponseDTO responseDTO = customerSevice.getCustomerById(1);

        assertEquals("John",responseDTO.getName());
        assertEquals(1,responseDTO.getAttachmentDTOS().size());

    }

    @Test
    void getCustomerId_fail(){
        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                ()->customerSevice.getCustomerById(1)
        );
        assertEquals("Customer not found!",ex.getMessage());
    }
}
