package com.example.Project.service;

import com.example.Project.enums.BookingCategory;
import com.example.Project.model.BookingModel;
import com.example.Project.repository.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookinServices {

    @Autowired
    BookingRepo repo;

    //create
    public BookingModel createBooking(BookingModel booking ){

        if(booking.getCategory() == BookingCategory.ROOM ||booking.getCategory() == BookingCategory.WEDDING){

            boolean exits = repo.existsByCategoryAndStartDateLessThanAndEndDateGreaterThan(
                    booking.getCategory(),
                    booking.getStartDate(),
                    booking.getEndDate()
            );

            if(exits){
                throw new RuntimeException(booking.getCategory()+" already booked!");
            }

        }
        return repo.save(booking);
    }


    //
}
