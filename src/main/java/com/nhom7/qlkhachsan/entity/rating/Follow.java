package com.nhom7.qlkhachsan.entity.rating;

import com.nhom7.qlkhachsan.entity.BaseEntity;
import com.nhom7.qlkhachsan.entity.hotel.Hotel;
import com.nhom7.qlkhachsan.entity.user.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="follow")
public class Follow extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User userFollow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hotel_id", nullable = false)
    private Hotel hotelIsFollowed;
}
