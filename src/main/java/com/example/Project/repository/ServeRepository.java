package com.example.Project.repository;

import com.example.Project.enums.StaffRole;
import com.example.Project.model.ServeModel;
import com.example.Project.model.staff.StaffModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServeRepository extends JpaRepository<ServeModel,Integer> {

    @Query(value =  """
                select  * from Staff 
                            where role = ? and status = "AVAILABLE"
                            limit 1
            """,nativeQuery = true)
    StaffModel findStaff(StaffRole role);
}
