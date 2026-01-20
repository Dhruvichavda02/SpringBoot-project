package com.example.Project.repository;

import com.example.Project.enums.BookingCategory;
import com.example.Project.model.BookingModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepo extends JpaRepository<BookingModel,Integer> {

    //room : cron job to checkout at 10:00 AM

    @Query(value = """

            SELECT *
        FROM booking
        WHERE status = 'CONFIRMED'
         and category in('ROOM')
          AND end_date = CURRENT_DATE
        """, nativeQuery = true)
    List<BookingModel> findExpiredBookings();

    @Query(value = """
    select Coalesce(SUM(b.quantity),0)
    from booking b 
    where b.resource_id = :resourceId
    and b.status = 'CONFIRMED'
    and(
        ( :startDate between b.start_date and b.end_date ) or
        (:endDate between b.start_date and b.end_date) or
        (b.start_date between :startDate and :endDate)
    )
""",nativeQuery = true)
    Integer getBookingQty(
      Integer resourceId,
      LocalDate startDate,
      LocalDate endDate
    );

}