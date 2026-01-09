package com.example.Project.repository;

import com.example.Project.model.staff.StaffAttachmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffAttachmentRepo extends JpaRepository<StaffAttachmentModel,Integer> {

    List<StaffAttachmentModel> findByStaff_IdAndActiveTrue(Integer staffId);
}
