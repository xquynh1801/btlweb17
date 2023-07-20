package com.nhom7.qlkhachsan.module.service;

import com.nhom7.qlkhachsan.app.entity.dto.StatDTO;
import com.nhom7.qlkhachsan.app.entity.dto.UserBookingDTO;

import java.util.List;

public interface BookingRoomService {
    List<UserBookingDTO> getAll();
    
    int countAll();

    int countUser();

    List<StatDTO> getTurnoversByMonth();
}
