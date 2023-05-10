package com.nhom7.qlkhachsan.dto;

import com.nhom7.qlkhachsan.entity.hotel.Room;
import com.nhom7.qlkhachsan.entity.user.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class BookingDTO {
    private String user_id;
    private String room_name;
    private String timeBegin;
    private String timeEnd;
    private Double price;
    private boolean isPaid;
}
