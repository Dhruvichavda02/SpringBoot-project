package com.example.Project.DTOs;

import com.example.Project.enums.StaffRole;

import java.time.LocalDate;
import java.util.List;

public class StaffResponseDTO {

    private Integer id;
    private String name;
    private LocalDate date_of_joining;
    private StaffRole role;
    private List<StaffAttachmentDTO> attachmentDTOS;

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

    public LocalDate getDate_of_joining() {
        return date_of_joining;
    }

    public void setDate_of_joining(LocalDate date_of_joining) {
        this.date_of_joining = date_of_joining;
    }

    public StaffRole getRole(StaffRole role) {
        return this.role;
    }

    public void setRole(StaffRole role) {
        this.role = role;
    }

    public List<StaffAttachmentDTO> getAttachmentDTOS() {
        return attachmentDTOS;
    }

    public void setAttachmentDTOS(List<StaffAttachmentDTO> attachmentDTOS) {
        this.attachmentDTOS = attachmentDTOS;
    }
}
