package com.example.Project.controller;

import com.example.Project.model.RestaurantTiming;
import com.example.Project.repository.RestaurantTImingRepository;
import com.example.Project.service.RestaurantTimingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/admin/restaurant")
public class RestaurantTimingController {

    @Autowired
    private RestaurantTImingRepository restaurantTImingRepository;


    @PutMapping
    public ResponseEntity<?> updateTiming(@RequestBody RestaurantTiming restiming){

        try {
            RestaurantTiming timing = restaurantTImingRepository.findById(1).orElse(new RestaurantTiming());

            timing.setOpenTime(restiming.getOpenTime());
            timing.setCloseTime(restiming.getCloseTime());
            timing.setOpen(restiming.isOpen());
            return new ResponseEntity<>(restaurantTImingRepository.save(timing),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }



}
