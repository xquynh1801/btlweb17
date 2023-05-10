package com.nhom7.qlkhachsan.repository;

import com.nhom7.qlkhachsan.entity.rating.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByHotelIsFollowedIdAndUserFollowId(Long hotelId, Long userId);
}
