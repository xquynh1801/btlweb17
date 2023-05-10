package com.nhom7.qlkhachsan.repository;

import com.nhom7.qlkhachsan.entity.rating.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserCommentIdAndHotelIsCommentedId(Long userId, Long hotelId);

//    @Query(value = "select * from comment c where c.hotel_id = hotelId order by id desc limit 1", nativeQuery = true)
//    Comment getCommentLastest(Long hotelId);

//    Comment findTopByHotelIsCommentedAndUserCommentOrderByIdDesc(Long hotelId, Long userId);
//
    Comment findByHotelIsCommentedId(Long hotelId);
}
