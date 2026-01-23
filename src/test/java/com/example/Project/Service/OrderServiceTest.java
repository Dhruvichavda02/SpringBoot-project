package com.example.Project.Service;


import com.example.Project.DTOs.OrderItemRequest;
import com.example.Project.DTOs.OrderResponseDTO;
import com.example.Project.model.MenuModel;
import com.example.Project.model.OrderItemsModel;
import com.example.Project.model.OrderModel;
import com.example.Project.model.customer.Customer;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.repository.MenuRepository;
import com.example.Project.repository.OrderItemRepository;
import com.example.Project.repository.OrderRepository;
import com.example.Project.service.OrderService;
import com.example.Project.service.RestaurantTimingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RestaurantTimingService timingServices;

    @InjectMocks
    private OrderService orderService;

    private OrderItemRequest request;
    private Customer customer;
    private OrderModel order;
    private MenuModel menu;

    @BeforeEach
    void setUp(){
        request = new OrderItemRequest();
        request.setQuantity(2);
        request.setMenuId(1);

        customer = new Customer();
        customer.setId(1);

        order = new OrderModel();
        order.setId(1);
        order.setCustomerId(customer);
        order.setTable_code("T1");
        order.setAmount(200.0);
        order.setActive(true);
        order.setItems(new ArrayList<>());

        menu = new MenuModel();
        menu.setId(1);
        menu.setPrice(100.0);
    }

    @Test
    void createOrder_success(){
        List<OrderItemRequest> items = List.of(request);
        when(timingServices.isRestaurantOpen()).thenReturn(true);
        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(customer));
        when(menuRepository.findByIdAndActive(1)).thenReturn(Optional.of(menu));
        when(orderRepository.save(any(OrderModel.class))).thenAnswer(i->i.getArgument(0));

        OrderResponseDTO res = orderService.createOrder(1,"T1",items);

        assertNotNull(res);
        assertEquals(200.0,res.getAmount());
        assertEquals("T1",res.getTableCode());
        assertEquals(1,res.getItems().size());
        verify(timingServices, times(1)).isRestaurantOpen();
        verify(customerRepository, times(1)).findByIdAndActiveTrue(1);
        verify(menuRepository, times(1)).findByIdAndActive(1);
        verify(orderRepository, times(2)).save(any(OrderModel.class));

    }

    @Test
    void createOrder_RestaurantClosed(){
        List<OrderItemRequest> items = List.of(request);

        when(timingServices.isRestaurantOpen()).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,()->orderService.createOrder(1,"T1",items));

        assertEquals("Restaurant is currently closed. Please order during working hours.",ex.getMessage());
        verify(timingServices, times(1)).isRestaurantOpen();
        verify(customerRepository, never()).findByIdAndActiveTrue(1);
        verify(menuRepository,never()).findByIdAndActive(1);
        verify(orderRepository, never()).save(any(OrderModel.class));

    }

    @Test
    void createOrder_customerNotFound(){
        List<OrderItemRequest> items = List.of(request);

        when(timingServices.isRestaurantOpen()).thenReturn(true);
        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,()->orderService.createOrder(1,"T1",items));

        assertEquals("Customer not found",ex.getMessage());
        verify(timingServices, times(1)).isRestaurantOpen();
        verify(customerRepository, times(1)).findByIdAndActiveTrue(1);
        verify(menuRepository,never()).findByIdAndActive(1);
        verify(orderRepository, never()).save(any(OrderModel.class));
    }


    @Test
    void createOrder_menuNotFound() {

        when(timingServices.isRestaurantOpen()).thenReturn(true);
        when(customerRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(customer));
        when(menuRepository.findByIdAndActive(1)).thenReturn(Optional.empty());
        when(orderRepository.save(any(OrderModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> orderService.createOrder(1, "T1", List.of(request)));

        assertEquals("Menu not found", ex.getMessage());

        verify(orderRepository, times(1)).save(any(OrderModel.class));
        verify(menuRepository, times(1)).findByIdAndActive(1);
    }

    @Test
    void getAllActiveOrders_success(){

        OrderItemsModel item = new OrderItemsModel();
        item.setMenuId(menu);
        item.setQuantity(2);
        item.setPrice(100.0);
        order.setItems(List.of(item));

        when(orderRepository.findActiveOrdersByTable("T1")) .thenReturn(List.of(order));

        List<OrderResponseDTO> result = orderService.getAllActiveOrders("T1");

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findActiveOrdersByTable("T1");
    }

    @Test
    void getCustomerOrder_success(){
        OrderItemsModel item = new OrderItemsModel();
        item.setMenuId(menu);
        item.setQuantity(2);
        item.setPrice(100.0);
        order.setItems(List.of(item));
        when(orderRepository.findByCustomer(1)).thenReturn(List.of(order));

        List<OrderResponseDTO> result = orderService.getCustomerOrder(1);

        assertNotNull(result);
        assertEquals(1,result.size());

        OrderResponseDTO dto = result.get(0);

        assertEquals(1,dto.getId());
        assertEquals(1,dto.getCustomerId());

        verify(orderRepository,times(1)).findByCustomer(1);
    }

    @Test
    void updateOrderItems_success(){
        OrderItemRequest updatedrequest = new OrderItemRequest();
        updatedrequest.setQuantity(1);
        updatedrequest.setMenuId(1);


        List<OrderItemRequest> items = List.of(updatedrequest);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(menuRepository.findByIdAndActive(1)).thenReturn(Optional.of(menu));
        when(orderItemRepository.save(any(OrderItemsModel.class))).thenAnswer(i->i.getArgument(0));
        when(orderRepository.save(any(OrderModel.class))).thenAnswer(i->i.getArgument(0));
        doNothing().when(orderItemRepository).deleteByOrderId(1);

        OrderResponseDTO  res = orderService.updateOrderItems(1,items);

        assertNotNull(res);
        assertEquals(100.0,res.getAmount());

        verify(orderRepository,times(1)).findById(1);
        verify(menuRepository,times(1)).findByIdAndActive(1);
        verify(orderItemRepository,times(1)).save(any(OrderItemsModel.class));
        verify(orderRepository,times(1)).save(any(OrderModel.class));


    }

    @Test
    void updateOrderItems_orderNotFound(){
        List<OrderItemRequest> items = List.of(request);

        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,()->orderService.updateOrderItems(1,items));

        assertEquals("No order found!",ex.getMessage());
        verify(orderRepository,times(1)).findById(1);
        verify(menuRepository,never()).findByIdAndActive(1);
        verify(orderItemRepository,never()).save(any(OrderItemsModel.class));
        verify(orderRepository,never()).save(any(OrderModel.class));

    }

    @Test
    void updateOrderItems_orderIsInactive(){
        order.setActive(false);
        List<OrderItemRequest> items = List.of(request);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        RuntimeException ex = assertThrows(RuntimeException.class,()->orderService.updateOrderItems(1,items));

        assertEquals("order is InActive",ex.getMessage());
        verify(orderRepository,times(1)).findById(1);
        verify(menuRepository,never()).findByIdAndActive(1);
        verify(orderItemRepository,never()).save(any(OrderItemsModel.class));
        verify(orderRepository,never()).save(any(OrderModel.class));

    }

    @Test
    void updateOrderItems_menuNotFound(){
        List<OrderItemRequest> items = List.of(request);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(menuRepository.findByIdAndActive(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,()->orderService.updateOrderItems(1,items));

        assertEquals("No such Menu available!",ex.getMessage());
        verify(orderRepository,times(1)).findById(1);
        verify(menuRepository,times(1)).findByIdAndActive(1);
        verify(orderItemRepository,never()).save(any(OrderItemsModel.class));
        verify(orderRepository,never()).save(any(OrderModel.class));

    }

    @Test
    void deactivatedOrder_success(){
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        orderService.deactivatedOrder(1);

        assertFalse(order.getActive());
    }

    @Test
    void deactivatedOrder_orderNotFound(){
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,()->orderService.deactivatedOrder(1));
        assertEquals("No such order exist!",ex.getMessage());
    }


}
