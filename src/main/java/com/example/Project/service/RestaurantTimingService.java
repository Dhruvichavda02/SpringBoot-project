package com.example.Project.service;

import com.example.Project.model.RestaurantTiming;
import com.example.Project.repository.RestaurantTImingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class RestaurantTimingService {

    @Autowired
    private RestaurantTImingRepository restaurantTImingRepository;

    //to check if the restaurant is open or not
    public boolean isRestaurantOpen(){

        RestaurantTiming timing  =restaurantTImingRepository.findById(1).orElseThrow(()-> new RuntimeException("No Timming set!"));

        if(!timing.isOpen()){
            return false;
        }

        LocalTime now = LocalTime.now();
        return !now.isAfter(timing.getCloseTime()) && !now.isBefore(timing.getOpenTime());
    }
}
