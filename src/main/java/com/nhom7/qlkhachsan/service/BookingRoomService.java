package com.nhom7.qlkhachsan.service;

import com.nhom7.qlkhachsan.dto.StatDTO;
import com.nhom7.qlkhachsan.dto.UserBookingDTO;

import java.util.List;

public interface BookingRoomService {
    List<UserBookingDTO> getAll();
    
    int countAll();

    int countUser();

    List<StatDTO> getTurnoversByMonth();
}
