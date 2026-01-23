package com.example.Project.model;
import com.example.Project.enums.StaffRole;
import com.example.Project.enums.TaskType;
import com.example.Project.model.customer.Customer;
import com.example.Project.model.staff.StaffModel;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ServeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="staff_id")
    private StaffModel staff;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private BookingModel booking;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Column(name="serve_at")
    private LocalDateTime serveAt;

    @Column(name = "Manager")
    private Integer manager_id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public StaffModel getStaff() {
        return staff;
    }

    public void setStaff(StaffModel staff) {
        this.staff = staff;
    }

    public LocalDateTime getServeAt() {
        return serveAt;
    }

    public void setServeAt(LocalDateTime serveAt) {
        this.serveAt = serveAt;
    }

    public BookingModel getBooking() {
        return booking;
    }

    public void setBooking(BookingModel booking) {
        this.booking = booking;
    }

    public TaskType getServeActionType() {
        return taskType;
    }

    public void setServeActionType(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Integer getManager_id() {
        return manager_id;
    }

    public void setManager_id(Integer manager_id) {
        this.manager_id = manager_id;
    }
}
