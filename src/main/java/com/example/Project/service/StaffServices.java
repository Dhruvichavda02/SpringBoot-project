package com.example.Project.service;


import com.example.Project.DTOs.StaffAttachmentDTO;
import com.example.Project.DTOs.StaffRequest;
import com.example.Project.DTOs.StaffResponseDTO;
import com.example.Project.enums.StaffRole;
import com.example.Project.model.staff.StaffModel;
import com.example.Project.model.UserMST;
import com.example.Project.repository.StaffAttachmentRepo;
import com.example.Project.repository.StaffRepo;
import com.example.Project.repository.UserMstRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServices {

    @Autowired
    StaffRepo staffRepo;

    @Autowired
    UserMstRepo userMstRepo;

    @Autowired
    StaffAttachmentRepo attachmentRepo;

    //create
    public StaffModel createStaff(StaffRequest request) {

        UserMST user = userMstRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        StaffModel staff = new StaffModel();
        staff.setName(request.getName());
        staff.setRole(request.getRole());
        staff.setDate_of_joining(request.getDate_of_joining());
        staff.setUser(user);
        staff.setActive(true);

        return staffRepo.save(staff);
    }


    //Read
    public List<StaffModel> getAllStaff(){
         return staffRepo.findByActiveTrue();
    }


    //Update
    public StaffModel UpdateStaff(StaffRequest staff,Integer id){
        StaffModel existStaff = staffRepo.findByIdAndActiveTrue(id).orElseThrow(() -> new RuntimeException("Staff not found"));
        existStaff.setName(staff.getName());
        existStaff.setRole(staff.getRole());
        existStaff.setDate_of_joining(staff.getDate_of_joining());
        return staffRepo.save(existStaff);
    }


    //soft-delete
        public String deactiveStaff(Integer id){
            StaffModel staff = staffRepo.findByIdAndActiveTrue(id).orElseThrow(()-> new RuntimeException("User does not exist!"));

            staff.setActive(false);
            staffRepo.save(staff);
            return "Staff deactivated successfully";
    }

    //Filter by role
    public  List<StaffModel> FilterByRole(StaffRole role){
        return staffRepo.findByRoleAndActiveTrue(role);
    }


    //Read by id
    public StaffResponseDTO getStaffById(Integer id) {

        StaffModel staff = staffRepo.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Staff not registered"));

        List<StaffAttachmentDTO> attachments =
                attachmentRepo.findByStaff_IdAndActiveTrue(staff.getId())
                        .stream()
                        .map(att -> {
                            StaffAttachmentDTO dto = new StaffAttachmentDTO();
                            dto.setId(att.getId());
                            dto.setPath(att.getPath());
                            dto.setActive(att.getActive());
                            return dto;
                        })
                        .toList();

        StaffResponseDTO res = new StaffResponseDTO();
        res.setId(staff.getId());
        res.setName(staff.getName());
        res.setRole(staff.getRole());
        res.setDate_of_joining(staff.getDate_of_joining());
        res.setAttachmentDTOS(attachments);

        return res;
    }
}
