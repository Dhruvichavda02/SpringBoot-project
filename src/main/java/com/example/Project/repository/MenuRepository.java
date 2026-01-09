package com.example.Project.repository;

import com.example.Project.model.MenuModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<MenuModel,Integer> {

    //find by id & active
    @Query(value = """
    select  * from menu where id = :id and active = true""",nativeQuery = true)
    Optional<MenuModel> findByIdAndActive(@Param("id") Integer id);

    //find all active menu
    @Query(value = """
    select * from menu where active = true order by name""",nativeQuery = true)
    List<MenuModel> findAllActive();

    //check if name exist
    @Query(value = """
    select case 
    when count(*) >0 then true
    else false 
    end
    from menu
    where name = :name   
""",nativeQuery = true)
    boolean existsByName(@Param("name") String name);

}
