package com.example.Project.repository;

import com.example.Project.model.CustomerAttachment;
import com.example.Project.model.StaffAttachmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffAttachment extends JpaRepository<StaffAttachmentModel,Integer> {

    List<StaffAttachmentModel> findByStaff_User_IdAndActiveTrue(Integer userId);

}
