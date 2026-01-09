package com.example.Project.model;

import com.example.Project.enums.PaymentStatus;
import com.example.Project.model.customer.Customer;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
     private Customer customerId;

    @Column(name="table_code",nullable = false)
    private String table_code;

    private LocalDateTime orderTime;
    private Double totalAmount;
    private Boolean active ;

    @Enumerated(EnumType.STRING)
    @Column(name = "payments_status")
    private PaymentStatus paymentStatus;

    @OneToMany(mappedBy = "orderId",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<OrderItemsModel> items;

    // automatically sets value before insert
    @PrePersist
    public void PrePersist(){
        this.orderTime  = LocalDateTime.now();
        this.totalAmount = 0.0;
        this.active = true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public String getTable_code() {
        return table_code;
    }

    public void setTable_code(String table_code) {
        this.table_code = table_code;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public Double getAmount() {
        return totalAmount;
    }

    public void setAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }



    public List<OrderItemsModel> getItems() {
        return items;
    }

    public void setItems(List<OrderItemsModel> items) {
        this.items = items;
    }
}
