package com.example.Project.controller;

import com.example.Project.DTOs.AssignTaskRequest;
import com.example.Project.enums.TaskType;
import com.example.Project.service.ServeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/serve")
public class ServeController {

    @Autowired
    private ServeService serveService;

    @PostMapping("/assign")
    public ResponseEntity<?> assignTask(  @RequestBody AssignTaskRequest request){


        try{
            serveService.assignTask( request.getRole(),
                    request.getBookingId(),
                    request.getManagerId());
            return ResponseEntity.ok("Tasks assigned successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }

    }
}
