package com.example.Project.service;

import com.example.Project.enums.BookingCategory;
import com.example.Project.enums.BookingStatus;
import com.example.Project.enums.PaymentStatus;
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
    BookingRepo bookingrepo;

    @Autowired
    private ResourceMstRepository resourceMstRepository;


    public BookingModel createBooking(BookingModel booking) {

        if(booking.getStartDate()== null || booking.getEndDate()== null){
            throw new RuntimeException("Please enter dates for booking");
        }

        if(booking.getStartDate().isBefore(LocalDate.now())){
            throw new RuntimeException("Booking on past date are not allowed");
        }
        //Table booking
        if (booking.getCategory() == BookingCategory.TABLE){
            booking.setEndDate(booking.getStartDate());
            if(booking.getQuantity()==null || booking.getQuantity()==0){
                booking.setQuantity(1);
            }
        }

        if(booking.getEndDate().isBefore(booking.getStartDate())){
            throw new RuntimeException("End Date should be greater than Start Date ");
        }



        if(booking.getQuantity()==null || booking.getQuantity()==0 ){
            throw new RuntimeException("Quantity is required!");
        }

        ResourceMstModel resource = resourceMstRepository.findAvailibilty(
                booking.getCategory().name(),
                booking.getCategory().name(),
                booking.getEndDate(),booking.getStartDate()
        );

        if (resource == null) {
            throw new RuntimeException("No " + booking.getCategory() + " available for selected dates");
        }

        //  Validate capacity properly
        if (booking.getQuantity() > resource.getCapacity()) {
            throw new RuntimeException(
                    "Capacity exceeded. Max allowed: " + resource.getCapacity()
            );
        }

        booking.setBookingDate(LocalDate.now());
        booking.setResourceId(resource);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setQuantity(booking.getQuantity());
        booking.setCapacity(booking.getCapacity());
        booking.setPaymentStatus(PaymentStatus.CREATED);
        return bookingrepo.save(booking);
    }

    //getById
    public BookingModel getByID(Integer id){
        return bookingrepo.findById(id).orElseThrow(()-> new RuntimeException("Booking not found"));
    }
    // cancel booking
    public void cancelBooking(Integer id){
        BookingModel booking = getByID(id);
        booking.setStatus(BookingStatus.CANCELLED);
        bookingrepo.save(booking);
    }

    public List<BookingModel> getAll(){
        return bookingrepo.findAll();
    }

    //cron usage

    public void completeExpiredBooking(){
        List<BookingModel> bookings = bookingrepo.findExpiredBookings();

        for (BookingModel booking : bookings) {
            booking.setStatus(BookingStatus.COMPLETED);
            bookingrepo.save(booking);
        }
    }

}
