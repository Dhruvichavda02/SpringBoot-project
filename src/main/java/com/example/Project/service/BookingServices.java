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

//    //create
//    public BookingModel createBooking(BookingModel booking ){
//
//        booking.setBookingDate(LocalDate.now());
//        if(booking.getCategory() == BookingCategory.ROOM ||booking.getCategory() == BookingCategory.WEDDING){
//
//            boolean exits = repo.existsByCategoryAndStartDateLessThanAndEndDateGreaterThan(
//                    booking.getCategory(),
//                    booking.getStartDate(),
//                    booking.getEndDate()
//            );
//
//            if(exits){
//                throw new RuntimeException(booking.getCategory()+" already booked!");
//            }
//
//        }
//        return repo.save(booking);
//    }
//
//
//    //read
//    public BookingModel getBookingById(Integer id){
//        return repo.findById(id).orElseThrow(()-> new RuntimeException("Booking not found!"));
//    }
//
//    public List<BookingModel> getAllBooking(){
//        return repo.findAll();
//    }
//
//    public List<BookingModel> getByCategory(BookingCategory category) {
//        return repo.findByCategory(category);
//    }
//    // Update
//    public BookingModel updateById(Integer id,BookingModel booking){
//
//        BookingModel existing = getBookingById(id);
//
//
//        existing.setCategory(booking.getCategory());
//        existing.setAmount(booking.getAmount());
//        existing.setBookingDate(booking.getBookingDate());
//        existing.setStartDate(booking.getStartDate());
//        existing.setEndDate(booking.getEndDate());
//
//        return repo.save(existing);
//    }
//
//    /* DELETE */
//    public void deleteBooking(Integer id) {
//        repo.deleteById(id);
//    }



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
