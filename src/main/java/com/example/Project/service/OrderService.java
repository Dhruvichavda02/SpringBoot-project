package com.example.Project.service;

import com.example.Project.DTOs.OrderItemRequest;
import com.example.Project.DTOs.OrderItemResponseDTO;
import com.example.Project.DTOs.OrderResponseDTO;
import com.example.Project.model.MenuModel;
import com.example.Project.model.OrderItemsModel;
import com.example.Project.model.OrderModel;
import com.example.Project.model.customer.Customer;
import com.example.Project.repository.CustomerRepo;
import com.example.Project.repository.MenuRepository;
import com.example.Project.repository.OrderItemRepository;
import com.example.Project.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private CustomerRepo customerRepo;

    //create order
    public OrderResponseDTO createOrder(Integer customerId, String tableCode, List<OrderItemRequest> items){
        //validate customer
        Customer customer = customerRepo.findByIdAndActiveTrue(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        OrderModel order = new OrderModel();
        order.setCustomerId(customer);
        order.setTable_code(tableCode);
        order.setActive(true);

        order = orderRepository.save(order);

        double total = 0;

        for (OrderItemRequest req : items) {

            MenuModel menu = menuRepository.findByIdAndActive(req.getMenuId())
                    .orElseThrow(() -> new RuntimeException("Menu not found"));

            OrderItemsModel item = new OrderItemsModel();
            item.setOrderId(order);
            item.setMenuId(menu);
            item.setQuantity(req.getQuantity());
            item.setPrice(menu.getPrice());

            total += req.getQuantity() * menu.getPrice();
            orderItemRepository.save(item);
        }

        order.setAmount(total);
        order = orderRepository.save(order);

        return mapToDTO(order);
    }

    //get all active orders by table
    public List<OrderResponseDTO> getAllActiveOrders(String TableCode){
        return orderRepository.findActiveOrdersByTable(TableCode)
                .stream()
                .map(this :: mapToDTO)
                .toList();
    }

    //get orders by customer
    public List<OrderResponseDTO> getCustomerOrder(Integer customerId){
        List<OrderModel> orders = orderRepository.findByCustomer(customerId);

        return orders.stream()
                .map(this::mapToDTO)
                .toList();
    }

    //update order
    public OrderResponseDTO updateOrderItems(Integer orderId , List<OrderItemRequest> items){

        OrderModel order = orderRepository.findById(orderId).orElseThrow(()-> new RuntimeException("No order found!"));

        if(!order.getActive()){
            throw new RuntimeException("order is InActive");
        }

        //delete existing item
        orderItemRepository.deleteByOrderId(orderId);

        double total =0 ;

        //insert new items
        for(OrderItemRequest req : items){

            MenuModel menu = menuRepository.findByIdAndActive(req.getMenuId()).orElseThrow(()->new RuntimeException("No such Menu available!"));
            OrderItemsModel item = new OrderItemsModel();

            item.setOrderId(order);
            item.setMenuId(menu);
            item.setQuantity(req.getQuantity());
            item.setPrice(menu.getPrice());

            total += req.getQuantity() * menu.getPrice();
            orderItemRepository.save(item);
        }

        order.setAmount(total);
        order= orderRepository.save(order);
        return mapToDTO(order);
    }

    //deactive
    public void deactivatedOrder(Integer orderId){
        OrderModel order = orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("No such order exist!"));

        order.setActive(false);
        orderRepository.save(order);

    }

    private OrderResponseDTO mapToDTO(OrderModel order){

        OrderResponseDTO dto = new OrderResponseDTO();

        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId().getId());
        dto.setTableCode(order.getTable_code());
        dto.setAmount(order.getAmount());
        dto.setActive(order.getActive());
        dto.setOrderTime(order.getOrderTime());

        List<OrderItemResponseDTO> itemsDTO = order.getItems().stream().map(item->{
            OrderItemResponseDTO i = new OrderItemResponseDTO();
            i.setMenuId(item.getMenuId().getId());
            i.setMenuName(item.getMenuId().getName());
            i.setQuantity(item.getQuantity());
            i.setPrice(item.getPrice());
            return i;

        }).toList();
        dto.setItems(itemsDTO);
        return dto;
    }
}
