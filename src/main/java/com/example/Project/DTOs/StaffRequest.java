package com.example.Project.DTOs;

import com.example.Project.enums.StaffRole;

import java.time.LocalDate;

public class StaffRequest {
    private String name;
    private LocalDate date_of_joining;
    private Integer userId;
    private StaffRole role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate_of_joining() {
        return date_of_joining;
    }

    public void setDate_of_joining(LocalDate date_of_joining) {
        this.date_of_joining = date_of_joining;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public StaffRole getRole() {
        return role;
    }

}
