package com.example.Project.Service;

import com.example.Project.DTOs.LoginRequest;
import com.example.Project.DTOs.LoginRes;
import com.example.Project.DTOs.RegisterRequest;
import com.example.Project.model.UserMST;
import com.example.Project.model.customer.Customer;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.repository.UserMstRepository;
import com.example.Project.service.AuthService;
import com.example.Project.service.JwtServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServicesTest {

    @Mock
    private UserMstRepository userMstRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtServices jwtServices;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private UserMST user;
    private Customer customer;
    private RegisterRequest request;

    @BeforeEach
    void setUp(){
        request = new RegisterRequest();
        request.setAddress("LA");
        request.setName("Joey");
        request.setContact("1234567890");
        request.setPassword("Hello@123");

        customer= new Customer();
        customer.setId(1);
        customer.setName("Joey");
        customer.setAddress("LA");
        customer.setContact("1234567890");
        customer.setActive(true);

        user = new UserMST();
        user.setUsername("Joey");
        user.setPassword("1234567890");

    }

    @Test
    void register_success(){
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(userMstRepository.saveAndFlush(any(UserMST.class))).thenReturn(user);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = authService.register(request);

        assertNotNull(result);
        assertEquals("Joey",result.getName());
        assertEquals("1234567890",result.getContact());
        verify(userMstRepository,times(1)).saveAndFlush(any(UserMST.class));
        verify(customerRepository,times(1)).save(any(Customer.class));
    }

    @Test
    void register_contactAlreadyExist(){
        when(customerRepository.existsByContact("1234567890")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,()->authService.register(request));

        assertEquals("Contact number already registered",ex.getMessage());
        verify(userMstRepository,never()).saveAndFlush(any(UserMST.class));
        verify(customerRepository,times(1)).existsByContact("1234567890");
        verify(customerRepository,never()).save(any(Customer.class));
    }

    @Test
    void login_success(){

        LoginRequest request = new LoginRequest();
        request.setContact("1234567890");
        request.setPassword("Hello@123");

        when(customerRepository.findByContactAndActiveTrue("1234567890")).thenReturn(customer);
        when(userMstRepository.findByUsername("1234567890")).thenReturn(Optional.of(user));
        when(encoder.matches("Hello@123","1234567890")).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(jwtServices.generateToken("1234567890")).thenReturn("fake-jwt-token");

        LoginRes res = authService.login(request);

        assertNotNull(res);
        assertEquals(customer,res.getCustomer());
        assertEquals("fake-jwt-token",res.getToken());
        verify(authenticationManager,times(1)).authenticate(any());

    }

    @Test
    void login_custometNotFound(){
        LoginRequest request = new LoginRequest();
        request.setContact("1234567890");
        request.setPassword("Hello@123");

        when(customerRepository.findByContactAndActiveTrue("1234567890")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,()->authService.login(request));

        assertEquals("Customer Not found with given username.", ex.getMessage());

    }

    @Test
    void login_passwordIncorrect(){
        LoginRequest request = new LoginRequest();
        request.setContact("1234567890");
        request.setPassword("wrongPassword");

        when(customerRepository.findByContactAndActiveTrue("1234567890")).thenReturn(customer);
        when(userMstRepository.findByUsername("1234567890")).thenReturn(Optional.of(user));
        when(encoder.matches("wrongPassword","1234567890")).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        assertEquals("Your password is incorrect.", exception.getMessage());

        verify(authenticationManager, never())
                .authenticate(any());
    }
}
