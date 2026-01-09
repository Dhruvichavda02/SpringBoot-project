package com.example.Project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "order_items")
public class OrderItemsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private OrderModel orderId;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuModel menuId;

    @NotNull(message = "quantity cannot be null!")
    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrderModel getOrderId() {
        return orderId;
    }

    public void setOrderId(OrderModel orderId) {
        this.orderId = orderId;
    }

    public MenuModel getMenuId() {
        return menuId;
    }

    public void setMenuId(MenuModel menuId) {
        this.menuId = menuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
