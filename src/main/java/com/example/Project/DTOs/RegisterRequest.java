package com.example.Project.DTOs;

import jakarta.validation.constraints.Pattern;

public class RegisterRequest {

    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$",
            message = "Password must be min 8 and max 20 length, containing at least 1 uppercase, 1 lowercase, 1 digit"
    )
    private String password;

    @Pattern(regexp = "\\d{10}",message = "Phone number requires 10 digit")
    private String contact;
    private String address;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
