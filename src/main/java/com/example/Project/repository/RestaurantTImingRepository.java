package com.example.Project.repository;

import com.example.Project.model.RestaurantTiming;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantTImingRepository extends JpaRepository<RestaurantTiming,Integer> {
}
