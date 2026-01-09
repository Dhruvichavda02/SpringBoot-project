package com.example.Project.service;


import com.example.Project.enums.BookingStatus;
import com.example.Project.model.BookingModel;
import com.example.Project.repository.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class BookingCronJob {

    @Autowired
    private BookingServices bookingServices;

    //Everday at 10 AM checkout
    @Scheduled(cron = "0 0 10 * * ?",zone = "Asia/Kolkata")
    //cron usage
    public void checkoutAtTen(){

        bookingServices.completeExpiredBooking();
    }
}
