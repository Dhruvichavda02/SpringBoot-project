package com.example.Project.repository;

import com.example.Project.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel,Integer> {

    //get active orders for a table
    @Query(value = """
        select * from orders where table_code = :tableCode and active= true
""",nativeQuery = true)
    List<OrderModel> findActiveOrdersByTable( String tableCode);


    //get customer order
    @Query(value = """
    select * from orders where customer_id = :customerId and active =true
""",nativeQuery = true)
    List<OrderModel> findByCustomer(Integer customerId);

    //find by tableCode
    @Query(value = """
    select  * from orders where id = :orderId and active = true
""",nativeQuery = true)
    Optional<OrderModel> findActiveOrdersById(Integer orderId);



}
