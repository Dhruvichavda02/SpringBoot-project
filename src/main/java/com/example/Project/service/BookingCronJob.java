package com.example.Project.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
