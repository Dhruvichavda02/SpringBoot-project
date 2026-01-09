package com.example.Project.controller;

import com.example.Project.DTOs.LoginRequest;
import com.example.Project.DTOs.RegisterRequest;
import com.example.Project.model.ResourceMstModel;
import com.example.Project.model.customer.Customer;
import com.example.Project.service.ResourceMstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceMstController {

    @Autowired
    private ResourceMstService resourceMstService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody ResourceMstModel model) {
        try {
            return new ResponseEntity<>(resourceMstService.create(model), HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/all")
    public List<ResourceMstModel> getAll(){
        return  resourceMstService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getResourceById(@PathVariable Integer id){
        return new ResponseEntity(resourceMstService.getById(id),HttpStatus.OK);
    }

    //Update
    @PutMapping("/{id}")
    public  ResponseEntity update( @RequestBody ResourceMstModel model,@PathVariable Integer id){
        try{
            return new ResponseEntity(resourceMstService.updateById(id,model) ,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.OK);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity DeactiveCustomer(@PathVariable Integer id){
        try{
            return new ResponseEntity(resourceMstService.deleteById(id),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
