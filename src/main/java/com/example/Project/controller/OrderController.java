package com.example.Project.controller;

import com.example.Project.DTOs.CreateOrderRequest;
import com.example.Project.DTOs.OrderItemRequest;
import com.example.Project.model.OrderModel;
import com.example.Project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")

public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request){
        try{
            return new ResponseEntity<>(orderService.createOrder(request.getCustomerId(),request.getTableCode(),request.getItems()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    // get all active orders by table code
    @GetMapping("/table/{tableCode}")
    public ResponseEntity<?> getAllActiveByTableCode(@PathVariable String tableCode){
        try{
            return new ResponseEntity<>(orderService.getAllActiveOrders(tableCode), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getOrderByCustomer(@PathVariable Integer customerId){
        try{
            return new ResponseEntity<>(orderService.getCustomerOrder(customerId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrderItems(@PathVariable Integer orderId, @RequestBody List<OrderItemRequest> items){
        try{
            return new ResponseEntity<>(orderService.updateOrderItems(orderId,items), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{orderId}")

    public ResponseEntity<?> deactivated(@PathVariable Integer orderId){
        try{
            orderService.deactivatedOrder(orderId);
            return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
