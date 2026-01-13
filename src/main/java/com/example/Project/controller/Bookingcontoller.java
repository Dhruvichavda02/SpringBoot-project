package com.example.Project.controller;

import com.example.Project.enums.BookingCategory;
import com.example.Project.model.BookingModel;
import com.example.Project.service.BookingServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name="bearerAuth")
@RestController
@RequestMapping("/bookings")
@Tag(name="Booking",description = "Booking Management API")
public class Bookingcontoller {

    @Autowired
    private BookingServices bookingService;

    @PostMapping("/create")
    @Operation(summary = "Creation of Booking")
    public ResponseEntity<?> createBooking(@RequestBody BookingModel booking) {
        try {
            return new ResponseEntity<>(bookingService.createBooking(booking), HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
        }


    @GetMapping("/getById/{id}")
    @Operation(summary = "Get Booking by Id")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        try {
            return new ResponseEntity<>(bookingService.getByID(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }

    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel Booking")
    public ResponseEntity<?> cancelBooking(@PathVariable Integer id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    }



}

