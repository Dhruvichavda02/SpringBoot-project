package com.example.Project.controller;

import com.example.Project.model.MenuModel;
import com.example.Project.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name="bearerAuth")
@RestController
@RequestMapping("/menu")
@Tag(name="Menu",description = "Menu Mangement API")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping
    @Operation(summary = "Menu Creation")
    public ResponseEntity<?> createMenu(@RequestBody MenuModel menu){
        try{
            return new ResponseEntity<>(menuService.createMenu(menu),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Menu By Id")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        try{
            return new ResponseEntity<>(menuService.getById(id),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/allactive")
    @Operation(summary = "Get All Active Menu")
    public ResponseEntity<?> getAllActive(){
        try{
            return new ResponseEntity<>(menuService.getAllActive(),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @PutMapping
    @Operation(summary = "Update Menu")
     public ResponseEntity<?>  updateMenu(@RequestBody MenuModel menu){
        try{
            return new ResponseEntity<>(menuService.updateMenu(menu),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Menu")
    public ResponseEntity<?>  DeleteMenu(@PathVariable Integer id){
        try{
            menuService.deactivated(id);
            return new ResponseEntity<>("Menu deleted Successfull",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }
}
