package com.example.Project.controller;

import com.example.Project.model.MenuModel;
import com.example.Project.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping
    public ResponseEntity<?> createMenu(@RequestBody MenuModel menu){
        try{
            return new ResponseEntity<>(menuService.createMenu(menu),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        try{
            return new ResponseEntity<>(menuService.getById(id),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/allactive")
    public ResponseEntity<?> getAllActive(){
        try{
            return new ResponseEntity<>(menuService.getAllActive(),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @PutMapping
     public ResponseEntity<?>  updateMenu(@RequestBody MenuModel menu){
        try{
            return new ResponseEntity<>(menuService.updateMenu(menu),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>  DeleteMenu(@PathVariable Integer id){
        try{
            menuService.deactivated(id);
            return new ResponseEntity<>("Menu deleted Successfull",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }
}
