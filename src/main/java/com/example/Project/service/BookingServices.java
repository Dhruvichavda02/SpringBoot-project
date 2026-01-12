package com.example.Project.service;

import com.example.Project.enums.BookingCategory;
import com.example.Project.enums.BookingStatus;
import com.example.Project.model.BookingModel;
import com.example.Project.model.ResourceMstModel;
import com.example.Project.repository.BookingRepo;
import com.example.Project.repository.ResourceMstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingServices {

    @Autowired
    BookingRepo Bookingrepo;

    @Autowired
    private ResourceMstRepository resourceMstRepository;


    public BookingModel createBooking(BookingModel booking) {

        //Table booking
        if (booking.getCategory() == BookingCategory.TABLE){
            booking.setEndDate(booking.getStartDate());
        }
        //find availability
        ResourceMstModel resource =  resourceMstRepository.findAvailibilty(
        booking.getCategory().name(),
        booking.getCategory().name(),
        booking.getEndDate(),booking.getStartDate());

        if(resource == null){
            throw new RuntimeException("No " + booking.getCategory() + " available for selected dates");
        }
        booking.setBookingDate(LocalDate.now());
        booking.setResourceId(resource);
        booking.setStatus(BookingStatus.CONFIRMED);
        return Bookingrepo.save(booking);
    }

    //getById
    public BookingModel getByID(Integer id){
        return Bookingrepo.findById(id).orElseThrow(()-> new RuntimeException("Booking not found"));
    }
    // cancel booking
    public void cancelBooking(Integer id){
        BookingModel booking = getByID(id);
        booking.setStatus(BookingStatus.CANCELLED);
        Bookingrepo.save(booking);
    }

    //cron usage

    public void completeExpiredBooking(){
        List<BookingModel> bookings = Bookingrepo.findExpiredBookings();

        for (BookingModel booking : bookings) {
            booking.setStatus(BookingStatus.COMPLETED);
            Bookingrepo.save(booking);
        }
    }

}
