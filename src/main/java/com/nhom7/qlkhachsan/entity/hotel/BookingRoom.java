package com.nhom7.qlkhachsan.entity.hotel;

import com.nhom7.qlkhachsan.entity.BaseEntity;
import com.nhom7.qlkhachsan.entity.user.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="booking")
public class BookingRoom extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User userBook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room roomIsBooked;

    @Column(nullable = false)
    private String timeBegin;

    @Column(nullable = false)
    private String timeEnd;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private boolean isPaid;
}
