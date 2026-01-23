package com.example.Project.Service;

import com.example.Project.enums.BookingCategory;
import com.example.Project.enums.BookingStatus;
import com.example.Project.enums.PaymentStatus;
import com.example.Project.model.BookingModel;
import com.example.Project.model.ResourceMstModel;
import com.example.Project.repository.BookingRepository;
import com.example.Project.repository.ResourceMstRepository;
import com.example.Project.service.BookingServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ResourceMstRepository resourceMstRepository;

    @InjectMocks
    private BookingServices bookingServices;

    private BookingModel booking;
    private ResourceMstModel resource;

    @BeforeEach
    void setUp(){
        resource = new ResourceMstModel();
        resource.setCode("101");
        resource.setActive(true);
        resource.setCapacity(4);
        resource.setId(1);
        resource.setCategory(BookingCategory.ROOM);
        List<ResourceMstModel> resourceList = List.of(resource);

        booking = new BookingModel();
        booking.setId(1);
        booking.setResourceId(resourceList);
        booking.setQuantity(1);
        booking.setBookingDate(LocalDate.now());
        booking.setCapacity(1);
        booking.setCustId(1);
        booking.setPaymentStatus(PaymentStatus.CREATED);
        booking.setCategory(BookingCategory.ROOM);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setStartDate(LocalDate.of(2026,3,20));
        booking.setEndDate(LocalDate.of(2026,3,20));

    }

    @Test
    void createBooking_success(){

        List<ResourceMstModel> resourceList = List.of(resource);

        when(bookingRepository.save(booking)).thenAnswer(i->i.getArgument(0));
        when(resourceMstRepository.findAvailibilty(booking.getCategory().name(), booking.getCategory().name(), booking.getEndDate(),booking.getStartDate())).thenReturn(resourceList);

        BookingModel result = bookingServices.createBooking(booking);

        assertEquals(1,result.getId());
        assertEquals(1,result.getQuantity());
        verify(bookingRepository,times(1)).save(any(BookingModel.class));
    }

    @Test
    void createBooking_NullDates(){
        booking.setEndDate(null);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingServices.createBooking(booking));

        assertEquals("Please enter dates for booking",ex.getMessage());
        verify(bookingRepository,never()).save(any(BookingModel.class));
    }

    @Test
    void createBooking_PastDate(){
        booking.setStartDate(LocalDate.of(2025,3,20));
        booking.setEndDate(LocalDate.of(2025,3,20));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingServices.createBooking(booking));

        assertEquals("Booking on past date are not allowed",ex.getMessage());
        verify(bookingRepository,never()).save(any(BookingModel.class));
    }
    @Test
    void createBooking_StartDateIsGreater(){
        booking.setStartDate(LocalDate.of(2026,3,20));
        booking.setEndDate(LocalDate.of(2026,3,7));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingServices.createBooking(booking));

        assertEquals("End Date should be greater than Start Date ",ex.getMessage());
        verify(bookingRepository,never()).save(any(BookingModel.class));
    }

    @Test
    void createBooking_nullQuantity(){
        booking.setQuantity(null);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingServices.createBooking(booking));

        assertEquals("Quantity is required!",ex.getMessage());
        verify(bookingRepository,never()).save(any(BookingModel.class));
    }

    @Test
    void createBooking_capacityExceeded(){
        booking.setCapacity(10);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingServices.createBooking(booking));

        assertEquals("Capacity exceeded. Max allowed: 0",ex.getMessage());
        verify(bookingRepository,never()).save(any(BookingModel.class));
    }

    @Test
    void getById_success(){
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        BookingModel result = bookingServices.getByID(1);

        assertEquals(1,result.getId());
        verify(bookingRepository,times(1)).findById(1);
    }

    @Test
    void getById_fail(){
        when(bookingRepository.findById(1)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingServices.getByID(1));
        assertEquals("Booking not found",ex.getMessage());
        verify(bookingRepository,times(1)).findById(1);
    }

    @Test
    void cancelBooking_success(){
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        bookingServices.cancelBooking(1);

        assertEquals(BookingStatus.CANCELLED,booking.getStatus());

    }

    @Test
    void cancelBooking_fail(){
        when(bookingRepository.findById(1)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingServices.cancelBooking(1));
        assertEquals("Booking not found",ex.getMessage());
        verify(bookingRepository,times(1)).findById(1);

    }

    @Test
    void getAll_success(){
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<BookingModel> result = bookingServices.getAll();

        assertEquals(1,result.size());
        verify(bookingRepository,times(1)).findAll();
    }

    @Test
    void completeExpiredBooking(){
        when(bookingRepository.findExpiredBookings()).thenReturn(List.of(booking));

        bookingServices.completeExpiredBooking();

        assertEquals(BookingStatus.COMPLETED, booking.getStatus());
        verify(bookingRepository, times(1)).save(any(BookingModel.class));
        verify(bookingRepository, times(1)).findExpiredBookings();
    }

}
