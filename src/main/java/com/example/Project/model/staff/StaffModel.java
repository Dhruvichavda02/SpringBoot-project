package com.example.Project.model.staff;


import com.example.Project.enums.StaffRole;
import com.example.Project.enums.StaffStatus;
import com.example.Project.model.UserMST;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "Staff")
public class StaffModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private  String name;
    private Boolean active = true;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private LocalDate date_of_joining;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserMST user;

    @Enumerated(EnumType.STRING)
    private StaffRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StaffStatus status= StaffStatus.AVAILABLE;

    public StaffModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDate getDate_of_joining() {
        return date_of_joining;
    }

    public void setDate_of_joining(LocalDate date_of_joining) {
        this.date_of_joining = date_of_joining;
    }

    public UserMST getUser() {
        return user;
    }

    public void setUser(UserMST user) {
        this.user = user;
    }

    public StaffRole getRole() {
        return role;
    }

    public void setRole(StaffRole role) {
        this.role = role;
    }

    public StaffStatus getStatus() {
        return status;
    }

    public void setStatus(StaffStatus status) {
        this.status = status;
    }
}
