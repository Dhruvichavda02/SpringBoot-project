package com.example.Project.DTOs;

import java.util.List;

public class CustomerAttachmentResponseDTO {
    private Integer id;
    private String name;
    private String address;
    private String contact;
    private Boolean active ;

    private List<CustomerAttachmentDTO> attachmentDTOS;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<CustomerAttachmentDTO> getAttachmentDTOS() {
        return attachmentDTOS;
    }

    public void setAttachmentDTOS(List<CustomerAttachmentDTO> attachmentDTOS) {
        this.attachmentDTOS = attachmentDTOS;
    }
}
