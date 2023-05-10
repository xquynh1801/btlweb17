package com.nhom7.qlkhachsan.entity.hotel;

import com.nhom7.qlkhachsan.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "room")
public class Room extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String roomName;

    private String type;

    @Column(nullable = false)
    private Double price;

    private String description;

    private String imagePath;

    @Column(nullable = false)
    private boolean status; // false: Room was booked

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hotel_id", nullable = false)
    private Hotel hotelHasRooms;

    @OneToMany(mappedBy = "roomIsBooked")
    private Set<BookingRoom> bookingRoomSet;
}
