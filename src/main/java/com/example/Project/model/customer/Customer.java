package com.example.Project.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Table(name="customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Employee name cannot be blank")
    private String name;
    private String address;
    @Pattern(regexp = "\\d{10}",message = "Phone number requires 10 digit")
    private String contact;

    private Boolean active = true;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserMST user;

    @OneToMany
    private List<CustomerAttachment> attachments;


    public Customer() {
    }

    public Customer(Integer id, String name, String address, String contact, String password) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;

    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }




    public UserMST getUser() {
        return user;
    }

    public void setUser(UserMST user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Address='" + address + '\'' +
                ", contact='" + contact + '\'' +

                '}';
    }
}
