package com.example.Project.Service;

import com.example.Project.enums.StaffRole;
import com.example.Project.enums.TaskType;
import com.example.Project.model.BookingModel;
import com.example.Project.model.customer.Customer;
import com.example.Project.model.staff.StaffModel;
import com.example.Project.repository.BookingRepository;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.repository.ServeRepository;
import com.example.Project.repository.StaffRepository;
import com.example.Project.service.ServeService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServeServiceTest {

    @Mock
    private ServeRepository serveRepository;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ServeService serveService;

    private StaffModel chef;
    private StaffModel waiter;
    private StaffModel cleaner;
    private StaffModel manager;
    private BookingModel booking;
    private Customer customer;

    @BeforeEach
    void setUp(){
        chef = new StaffModel();
        chef.setRole(StaffRole.CHEF);

        waiter = new StaffModel();
        waiter.setRole(StaffRole.WAITER);

        cleaner = new StaffModel();
        cleaner.setRole(StaffRole.ROOM_CLEANER);

        manager = new StaffModel();
        manager.setRole(StaffRole.MANAGER);
        manager.setActive(true);
        manager.setId(1);

        booking = new BookingModel();
        booking.setId(1);
        booking.setCustId(1);

        customer = new Customer();
        customer.setId(1);
        customer.setActive(true);
    }

    @Test
    void assignTask_success(){

        when(staffRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(manager));
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(customer));
        when(staffRepository.findFirstByRoleAndActiveTrue(StaffRole.CHEF)).thenReturn(Optional.of(chef));
        when(staffRepository.findFirstByRoleAndActiveTrue(StaffRole.WAITER)).thenReturn(Optional.of(waiter));
        when(staffRepository.findFirstByRoleAndActiveTrue(StaffRole.ROOM_CLEANER)).thenReturn(Optional.of(cleaner));

        serveService.assignTask(TaskType.CLEAN, 1, 1);

        verify(staffRepository,times(1)).findByIdAndActiveTrue(1);
        verify(bookingRepository,times(1)).findById(1);
        verify(customerRepository,times(1)).findByIdAndActiveTrue(1);

    }
    @Test
    void assignTask_managerNotFound() {

        when(staffRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> serveService.assignTask(TaskType.COOK, 10, 1));

        assertEquals("Manager not found", ex.getMessage());
    }


    @Test
    void assignTask_bookingNotFound() {

        when(staffRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(manager));
        when(bookingRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> serveService.assignTask(TaskType.COOK, 1, 1));
        assertEquals("Booking not found", ex.getMessage());

        verify(staffRepository,times(1)).findByIdAndActiveTrue(1);

    }

    @Test
    void assignTask_customerNotFound() {

        when(staffRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(manager));
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> serveService.assignTask(TaskType.COOK, 1, 1));
        assertEquals("Customer not found", ex.getMessage());

        verify(staffRepository,times(1)).findByIdAndActiveTrue(1);

    }

    @Test
    void assignTask_notAManager(){

        StaffModel staff = new StaffModel();
        staff.setRole(StaffRole.CHEF);
        staff.setActive(true);

        when(staffRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(staff));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> serveService.assignTask(TaskType.COOK, 1, 1));

        assertEquals("Only Manager can assign the role !", ex.getMessage());

    }

    @Test
    void assignTask_chefNotAvailable() {

        when(staffRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(manager));
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(customer));
        when(staffRepository.findFirstByRoleAndActiveTrue(StaffRole.CHEF)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> serveService.assignTask(TaskType.COOK, 1, 1));

        assertEquals("Chef not available", ex.getMessage());
    }

    @Test
    void assignTask_waiterNotAvailable() {

        when(staffRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(manager));
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(customer));
        when(staffRepository.findFirstByRoleAndActiveTrue(StaffRole.CHEF)).thenReturn(Optional.of(chef));
        when(staffRepository.findFirstByRoleAndActiveTrue(StaffRole.WAITER)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> serveService.assignTask(TaskType.SERVE, 1, 1));

        assertEquals("Waiter not available", ex.getMessage());
    }

    @Test
    void assignTask_cleanerNotAvailable() {

        when(staffRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(manager));
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(customer));
        when(staffRepository.findFirstByRoleAndActiveTrue(StaffRole.CHEF)).thenReturn(Optional.of(chef));
        when(staffRepository.findFirstByRoleAndActiveTrue(StaffRole.WAITER)).thenReturn(Optional.of(waiter));
        when(staffRepository.findFirstByRoleAndActiveTrue(StaffRole.ROOM_CLEANER)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> serveService.assignTask(TaskType.CLEAN, 1, 1));

        assertEquals("Room_cleaner not available", ex.getMessage());
    }
}
