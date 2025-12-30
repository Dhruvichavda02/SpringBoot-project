package com.example.Project.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.BatchSize;
import org.jspecify.annotations.NonNull;

@Entity
@Table(name="customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Employee name cannot be blank")
    private String username;
    @Email(message = "Email should be valid")
    private String email;
    @Pattern(regexp = "\\d{10}",message = "Phone number requires 10 digit")
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Size(min=8)
    private String password;
    @Digits(fraction = 0, integer = 10, message ="add a digit msg")
    private Integer age;


    public Customer() {
    }

    public Customer(Integer id, String username, String email, String phoneNumber, String password,Integer age) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.age = age;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getpassword() {
        return password;
    }

    public void setpassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
