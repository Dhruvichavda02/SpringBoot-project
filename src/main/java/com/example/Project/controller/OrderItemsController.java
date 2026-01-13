package com.example.Project.controller;

import com.example.Project.DTOs.OrderItemRequest;
import com.example.Project.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name="bearerAuth")
@RestController
@RequestMapping("/order-items")
@Tag(name = "OrderItem",description = "OrderItem Management API")
public class OrderItemsController {

    @Autowired
    private OrderItemService orderItemService;

    // Add new item to an existing order
    @PostMapping("/addItem/{orderId}")
    @Operation(summary = "Add Item")
    public ResponseEntity<?> addItem(
            @PathVariable Integer orderId,
            @RequestBody OrderItemRequest request) {
        try {
            return new ResponseEntity<>(
                    orderItemService.addItemToOrder(orderId, request.getMenuId(), request.getQuantity()),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //  Update quantity of an existing item
    @PutMapping("/updateItem/{itemId}")
    @Operation(summary = "Update Item")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Integer itemId,
            @RequestBody OrderItemRequest req) {
        try {
            return new ResponseEntity<>(
                    orderItemService.updateItemQuantity(itemId, req.getQuantity()),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Remove an item from order
    @DeleteMapping("/remove/{itemId}")
    @Operation(summary = "Delete Item")
    public ResponseEntity<?> deleteItem(@PathVariable Integer itemId) {
        try {
            orderItemService.removeItem(itemId);
            return new ResponseEntity<>("Item removed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
