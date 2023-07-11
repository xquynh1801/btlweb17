package com.nhom7.qlkhachsan.dto;

import com.nhom7.qlkhachsan.entity.hotel.Room;
import com.nhom7.qlkhachsan.entity.rating.Comment;
import com.nhom7.qlkhachsan.entity.rating.Follow;
import com.nhom7.qlkhachsan.entity.user.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

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
