package com.example.Project.repository;


import com.example.Project.model.OrderItemsModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemsModel,Integer> {

    //get all items of a particular order_id
    @Query(value = """
    select * from order_items where order_id = :orderId
""",nativeQuery = true)
    List<OrderItemsModel> findByOrderId(Integer orderId);

    //for items updation
    @Modifying
    @Transactional
    @Query(value = """
    delete from order_items where order_id = :orderId
""",nativeQuery = true)
    void deleteByOrderId(Integer orderId);

}
