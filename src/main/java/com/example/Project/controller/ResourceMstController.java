package com.example.Project.controller;

import com.example.Project.DTOs.LoginRequest;
import com.example.Project.DTOs.RegisterRequest;
import com.example.Project.model.ResourceMstModel;
import com.example.Project.model.customer.Customer;
import com.example.Project.service.ResourceMstService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name="bearerAuth")
@RestController
@RequestMapping("/resources")
@Tag(name="Resource",description = "Resource Master Management")
public class ResourceMstController {

    @Autowired
    private ResourceMstService resourceMstService;


    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creation of Resource")
    public ResponseEntity create(@RequestBody ResourceMstModel model) {
        try {
            return new ResponseEntity<>(resourceMstService.create(model), HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all resources")
    public List<ResourceMstModel> getAll(){
        return  resourceMstService.getAll();
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "Get resource by id")
    public ResponseEntity getResourceById(@PathVariable Integer id){
        return new ResponseEntity(resourceMstService.getById(id),HttpStatus.OK);
    }

    //Update
    @PutMapping("/update/{id}")
    @Operation(summary = "Update resource")
    public  ResponseEntity update( @RequestBody ResourceMstModel model,@PathVariable Integer id){
        try{
            return new ResponseEntity(resourceMstService.updateById(id,model) ,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deactive resource")
    public ResponseEntity DeactiveCustomer(@PathVariable Integer id){
        try{
            return new ResponseEntity(resourceMstService.deleteById(id),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
