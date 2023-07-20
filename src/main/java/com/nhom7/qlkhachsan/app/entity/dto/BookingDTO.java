package com.nhom7.qlkhachsan.app.entity.dto;

import lombok.Data;

@Data
public class BookingDTO {
    private String user_id;
    private String room_name;
    private String timeBegin;
    private String timeEnd;
    private Double price;
    private boolean isPaid;
}
