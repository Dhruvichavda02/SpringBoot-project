package com.example.Project.repository;

import com.example.Project.model.UserMST;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMstRepo extends JpaRepository<UserMST,Integer> {

    Optional<UserMST> findByUsername(String username);
}
