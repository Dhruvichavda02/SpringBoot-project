package com.example.Project.repository;

import com.example.Project.enums.BookingCategory;
import com.example.Project.model.ResourceMstModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ResourceMstRepository extends JpaRepository<ResourceMstModel,Integer> {


   Optional<ResourceMstModel> findByIdAndActiveTrue(Integer id);

   @Query(value = """
    select r.*
    from resourcemst r
    where r.category = ?
    and r.active = true
    and r.id not in (
    select br.resource_id from booking_resource br
    join booking b on b.id = br.booking_id
        where b.category = ?
        and b.status = 'CONFIRMED'
        and(b.start_date <= ?
           and b.end_date >= ?
        )
    )
    
""",nativeQuery = true)
   List<ResourceMstModel> findAvailibilty(String resourceCategory,String bookingCategory,LocalDate endDate,LocalDate startDate);

 }
