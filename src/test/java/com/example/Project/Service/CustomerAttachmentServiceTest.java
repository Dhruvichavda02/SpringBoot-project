package com.example.Project.Service;

import com.example.Project.model.customer.Customer;
import com.example.Project.model.customer.CustomerAttachment;
import com.example.Project.repository.CustomerAttachmentsRepo;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.service.CustomerAttachmentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CustomerAttachmentServiceTest {

    @Mock
    private CustomerAttachmentsRepo customerAttachmentsRepo;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private CustomerAttachmentsService customerAttachmentsService;

    private Customer customer;

    @BeforeEach
    void setUp(){
        customer = new Customer();
        customer.setId(1);
        customer.setActive(true);
    }
    @Test
    void uploadFile_success() throws IOException {

        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(customer));
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
        doNothing().when(multipartFile).transferTo(any(File.class)); // When transferTo() is called on the mocked multipartFile with any File argument, do absolutely nothing
        when(customerAttachmentsRepo.save(any(CustomerAttachment.class))).thenAnswer(i-> i.getArgument(0));

        CustomerAttachment result = customerAttachmentsService.uploadFile(1,multipartFile);

        assertNotNull(result);
        assertEquals(customer,result.getCustomer());
        assertTrue(result.getPath().contains("test.pdf"));
        assertTrue(result.getActive());

        verify(customerRepository,times(1)).findByIdAndActiveTrue(1);
        verify(customerAttachmentsRepo,times(1)).save(any());
    }

    @Test
    void uploadFile_customerNotFound(){
        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> customerAttachmentsService.uploadFile(1, multipartFile));

        assertEquals("Customer not found!",ex.getMessage());
        verify(customerRepository,times(1)).findByIdAndActiveTrue(1);
        verify(customerAttachmentsRepo,never()).save(any());
    }
}
