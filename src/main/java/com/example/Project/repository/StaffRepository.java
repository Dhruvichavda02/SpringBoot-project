package com.example.Project.repository;

import com.example.Project.enums.StaffRole;
import com.example.Project.model.staff.StaffModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<StaffModel,Integer> {

    List<StaffModel> findByActiveTrue();

    Optional<StaffModel> findByIdAndActiveTrue(Integer id);

    List<StaffModel> findByRoleAndActiveTrue(StaffRole role);
    Optional<StaffModel> findByUser_IdAndActiveTrue(Integer userId);

    Optional<StaffModel> findFirstByRoleAndActiveTrue(StaffRole role);
}
