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
//    @Query(value = """
//       select  count(*) from booking b
//              where resource_id = :resourceId
//                     and b.status = 'CONFIRMED'
//                     and (b.start_date <:endDate
//                     and b.end_date > :startDate)
//       """,nativeQuery = true)
//    int checkOverlap(
//            Integer resourceId,
//            LocalDate startDate,
//            LocalDate endDate
//    );

    //room : cron job to checkout at 10:00 AM

    @Query(value = """
        SELECT *
        FROM booking
        WHERE status = 'CONFIRMED'
         and category in('ROOM','WEDDING')
          AND end_date = CURRENT_DATE
        """, nativeQuery = true)
    List<BookingModel> findExpiredBookings();


//    @Query(value = """
//    select * from booking where id = :bookingId and active= true
//""",nativeQuery = true)
//    Optional<BookingModel> findByIdAndActive(Integer bookingId);
}
