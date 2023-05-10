package com.nhom7.qlkhachsan.repository;

import com.nhom7.qlkhachsan.dto.StatDTO;
import com.nhom7.qlkhachsan.dto.UserBookingDTO;
import com.nhom7.qlkhachsan.entity.hotel.BookingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingRoom, Long> {
  
   @Query(value = "SELECT user.id as id, user.full_name as name, user.identity_card_number as identify_card_number" +
           ", user.phone_number as phoneNumber, " +
           "p.room_name as roomName, p.price as price, p.time_begin as timeBegin, p.time_end as timeEnd FROM user " +
           "JOIN (SELECT b.user_id, b.price,b.time_begin, b.time_end,r.room_name FROM booking b JOIN room r ON b.room_id = r.id) as p " +
           "ON user.id = p.user_id", nativeQuery = true)
   List<UserBookingDTO> getListUserBooked();
  
   @Query(value = "select count(id) from booking", nativeQuery = true)
   int countAll();

   @Query(value = "select count(distinct(user_id)) from booking", nativeQuery = true)
   int countUser();

   @Query(value = "SELECT MONTH(time_begin) AS month, SUM(price) AS total FROM booking GROUP BY MONTH(time_begin)  ORDER BY MONTH(time_begin) ", nativeQuery = true)
   List<StatDTO> getTurnoversByMonth();

   List<BookingRoom> getAllByUserBookId(Long useId);

}
