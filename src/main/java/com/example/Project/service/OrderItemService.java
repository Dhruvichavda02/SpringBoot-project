package com.example.Project.service;

import com.example.Project.model.MenuModel;
import com.example.Project.model.OrderItemsModel;
import com.example.Project.model.OrderModel;
import com.example.Project.repository.MenuRepository;
import com.example.Project.repository.OrderItemRepository;
import com.example.Project.repository.OrderRepository;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderItemRepository orderItemRepo;

    @Autowired
    private MenuRepository menuRepo;

    //get all item of the orderId
    public List<OrderItemsModel> getItemsByOrder(Integer orderId){
        return orderItemRepo.findByOrderId(orderId);
    }

    //adding a single item in an order or Increasing quantity
    public OrderItemsModel addItemToOrder(Integer orderId,Integer menuId,Integer quantity){
        OrderModel order = orderRepo.findById(orderId).orElseThrow(()-> new RuntimeException("Order does not exist"));

        MenuModel menu = menuRepo.findById(menuId).orElseThrow(()-> new RuntimeException("Menu does not exist"));

        OrderItemsModel items = new OrderItemsModel();
        items.setPrice(menu.getPrice());
        items.setQuantity(quantity);
        items.setOrderId(order);
        items.setMenuId(menu);

        OrderItemsModel savedItem= orderItemRepo.save(items);

        //update total
        order.setAmount(
                order.getAmount() + (quantity * menu.getPrice())
        );
        orderRepo.save(order);
        return savedItem;
    }


    //update
    public OrderItemsModel updateItemQuantity(Integer orderItemId,Integer quantity){

        OrderItemsModel items  = orderItemRepo.findById(orderItemId).orElseThrow(()-> new RuntimeException("No such Item exist"));

        OrderModel order = items.getOrderId();

        //adjust total
        double oldTotal = items.getQuantity() * items.getPrice();
        double newTotal = quantity * items.getPrice();
        order.setAmount(order.getAmount() - oldTotal + newTotal);
        items.setQuantity(quantity);

        orderRepo.save(order);
        return orderItemRepo.save(items);
    }

    //remove item
    public void removeItem(Integer orderItemId){

        OrderItemsModel item = orderItemRepo.findById(orderItemId).orElseThrow(()-> new RuntimeException("No such Item exist"));

        OrderModel order = item.getOrderId();

        double itemTotal = item.getQuantity() * item.getPrice();
        order.setAmount(order.getAmount() - itemTotal);

        orderItemRepo.delete(item);
        orderRepo.save(order);

    }


}
