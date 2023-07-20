package com.nhom7.qlkhachsan.app.entity.dto;

import lombok.Data;

@Data
public class HotelDTO {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String description;
    private String imagePath;
    private Long owner_id;
}
