package com.example.Project.controller;

import com.example.Project.DTOs.StaffRequest;
import com.example.Project.enums.StaffRole;
import com.example.Project.model.staff.StaffModel;
import com.example.Project.service.StaffAttachmentService;
import com.example.Project.service.StaffServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    StaffServices staffServices;

    @Autowired
    StaffAttachmentService staffAttachmentService;

    //create
    @PostMapping
    public ResponseEntity createStaff(@RequestBody StaffRequest request){
        try {
            return new ResponseEntity<>(staffServices.createStaff(request), HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // GET ALL ACTIVE STAFF
    @GetMapping("/all")
    public List<StaffModel> getAllStaff() {
        return staffServices.getAllStaff();
    }

    // GET STAFF BY ID
    @GetMapping("/{id:\\d+}")
    public ResponseEntity getStaffById(@PathVariable Integer id) {
        try {
            return new ResponseEntity(staffServices.getStaffById(id),HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // UPDATE STAFF
    @PutMapping("/{id:\\d+}")
    public ResponseEntity<?> updateStaff(
            @PathVariable Integer id,
            @RequestBody StaffRequest updatedStaff
    ) {
        try {
            return new ResponseEntity<>(staffServices.UpdateStaff( updatedStaff,id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // SOFT DELETE STAFF
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<?> deactivateStaff(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(staffServices.deactiveStaff(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // GET staff by filter
    @GetMapping("/{role}")
    public ResponseEntity<List<StaffModel>> getStaff(
            @PathVariable StaffRole role
    ) {
        if (role != null) {
            return ResponseEntity.ok(staffServices.FilterByRole(role));
        }
        return ResponseEntity.ok(staffServices.getAllStaff());
    }


    // upload attachment
    @PostMapping(value = "{id}/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadAttachment(@PathVariable Integer id, @RequestParam("file")MultipartFile file){
        try{
            return new ResponseEntity(staffAttachmentService.uploadFile(id,file),HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST) ;
        }
    }

}


