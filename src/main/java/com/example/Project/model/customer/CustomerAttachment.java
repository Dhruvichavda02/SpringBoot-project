package com.example.Project.model;


import jakarta.persistence.*;

@Entity
@Table(name = "customerattachment")
public class CustomerAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name="customerid")
    private Customer customer;

    private String path;

    public CustomerAttachment() {

    }


    public CustomerAttachment(Integer id, Boolean active, Customer customer, String path) {
        this.id = id;
        this.active = active;
        this.customer = customer;
        this.path = path;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
