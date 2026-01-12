package com.example.Project.controller;

import com.example.Project.model.RestaurantTiming;
import com.example.Project.repository.RestaurantTImingRepository;
import com.example.Project.service.RestaurantTimingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@SecurityRequirement(name="bearerAuth")
@RestController
@RequestMapping("/admin/restaurant")
@Tag(name="RestaurantTiming",description = "Restaurant Timing Management API")
public class RestaurantTimingController {

    @Autowired
    private RestaurantTImingRepository restaurantTImingRepository;


    @PutMapping
    @Operation(summary = "Update Timing")
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
