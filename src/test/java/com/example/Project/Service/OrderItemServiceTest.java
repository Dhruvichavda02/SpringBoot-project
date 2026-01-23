package com.example.Project.Service;

import com.example.Project.model.MenuModel;
import com.example.Project.model.OrderItemsModel;
import com.example.Project.model.OrderModel;
import com.example.Project.repository.MenuRepository;
import com.example.Project.repository.OrderItemRepository;
import com.example.Project.repository.OrderRepository;
import com.example.Project.service.OrderItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderItemService orderItemService;

    private OrderModel order ;
    private MenuModel menu;

    @BeforeEach
    void setUp(){


        order = new OrderModel();
        order.setId(1);
        order.setAmount(100.0);

        menu = new MenuModel();
        menu.setId(10);
        menu.setName("Pizza");
        menu.setPrice(50.0);
    }

    @Test
    void addItemToOrder_success(){

        Integer orderId = 1;
        Integer menuId = 10;
        Integer quantity = 2;

        OrderItemsModel savedItem = new OrderItemsModel();
        savedItem.setOrderId(order);
        savedItem.setQuantity(quantity);
        savedItem.setMenuId(menu);
        savedItem.setPrice(menu.getPrice());


        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(orderItemRepository.save(any(OrderItemsModel.class))).thenReturn(savedItem);
        when(orderRepository.save(any(OrderModel.class))).thenReturn(order);

        OrderItemsModel item = orderItemService.addItemToOrder(orderId,menuId,quantity);

        assertNotNull(item);
        assertEquals(quantity,item.getQuantity());
        assertEquals(50.0,item.getPrice());
        assertEquals(200.0,order.getAmount());

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(OrderModel.class));
        verify(menuRepository,times(1)).findById(menuId);
        verify(orderItemRepository,times(1)).save(any(OrderItemsModel.class));
    }

    @Test
    void addItemToOrder_orderDoesNotExist(){
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,()->orderItemService.addItemToOrder(1,20,2));

        assertEquals("Order does not exist",ex.getMessage());
        verify(orderRepository,times(1)).findById(1);
        verify(orderRepository,never()).save(any(OrderModel.class));
        verify(menuRepository,never()).findById(any());
        verify(orderItemRepository,never()).save(any(OrderItemsModel.class));

    }

    @Test
    void addItemToOrder_menuDoesNotExist(){
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(menuRepository.findById(10)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,()->orderItemService.addItemToOrder(1,10,2));

        assertEquals("Menu does not exist",ex.getMessage());
        verify(orderRepository,times(1)).findById(1);
        verify(menuRepository,times(1)).findById(10);
        verify(orderItemRepository,never()).save(any(OrderItemsModel.class));
    }

    @Test
    void updateItemQuantity_success(){
        OrderItemsModel items = new OrderItemsModel();
        items.setId(1);
        items.setQuantity(2);
        items.setOrderId(order);
        items.setPrice(100.0);

        when(orderItemRepository.findById(1)).thenReturn(Optional.of(items));
        when(orderItemRepository.save(any(OrderItemsModel.class))).thenAnswer(i->i.getArgument(0));

        OrderItemsModel result = orderItemService.updateItemQuantity(1,1);

        assertNotNull(result);
        assertEquals(1,result.getQuantity());
        verify(orderItemRepository,times(1)).findById(1);
        verify(orderItemRepository,times(1)).save(any(OrderItemsModel.class));

    }

    @Test
    void updateItemQuantity_fail(){
        when(orderItemRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,()->orderItemService.updateItemQuantity(1,1));

        assertEquals("No such Item exist",ex.getMessage());
        verify(orderItemRepository, times(1)).findById(1);
        verify(orderRepository, never()).findById(any());
        verify(orderRepository, never()).save(any(OrderModel.class));
        verify(menuRepository, never()).findById(any());
        verify(orderItemRepository, never()).save(any(OrderItemsModel.class));
    }

    @Test
    void removeItem_success(){
        OrderItemsModel items = new OrderItemsModel();
        items.setId(1);
        items.setQuantity(2);
        items.setOrderId(order);
        items.setPrice(50.0);

        when(orderItemRepository.findById(1)).thenReturn(Optional.of(items));

        orderItemService.removeItem(1);

        assertEquals(0.0,order.getAmount());
        verify(orderItemRepository, times(1)).findById(1);
        verify(orderRepository, times(1)).save(order);

    }


}

