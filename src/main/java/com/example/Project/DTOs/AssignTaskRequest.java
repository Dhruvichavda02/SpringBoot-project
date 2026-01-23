package com.example.Project.DTOs;


import com.example.Project.enums.TaskType;

public class AssignTaskRequest {
    private Integer managerId;
    private Integer bookingId;
    private TaskType role;

//    public Integer getStaffId() {
//        return staffId;
//    }
//
//    public void setStaffId(Integer staffId) {
//        this.staffId = staffId;
//    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public TaskType getRole() {
        return role;
    }

    public void setRole(TaskType role) {
        this.role = role;
    }
}
