package com.example.Project.model;

import com.example.Project.enums.BookingCategory;
import com.example.Project.enums.BookingStatus;
import com.example.Project.enums.PaymentStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "booking")
public class BookingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "customer_id" ,nullable = false)
    private Integer custId;


   @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingCategory category;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Double amount;

    @ManyToMany
    @JoinTable(name = "booking_resource",joinColumns = @JoinColumn(name="booking_id"),inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private List<ResourceMstModel> resourceId;

    @Enumerated(EnumType.STRING)
    @Column
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payments_status")
    private PaymentStatus paymentStatus;

    @Column(name = "quantity",nullable = false)
    private Integer quantity;

    @Column(name = "capacity",nullable = false)
    private Integer capacity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public BookingCategory getCategory() {
        return category;
    }

    public void setCategory(BookingCategory category) {
        this.category = category;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<ResourceMstModel> getResourceId() {
        return resourceId;
    }

    public void setResourceId(List<ResourceMstModel> resourceId) {
        this.resourceId = resourceId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
