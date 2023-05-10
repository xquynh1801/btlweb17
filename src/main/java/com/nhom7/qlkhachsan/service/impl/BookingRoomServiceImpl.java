package com.nhom7.qlkhachsan.service.impl;

import com.nhom7.qlkhachsan.dto.StatDTO;
import com.nhom7.qlkhachsan.dto.UserBookingDTO;
import com.nhom7.qlkhachsan.repository.BookingRepository;
import com.nhom7.qlkhachsan.service.BookingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookingRoomServiceImpl implements BookingRoomService {

    @Autowired
    BookingRepository bookingRepository;


    @Override
    public List<UserBookingDTO> getAll() {
        return bookingRepository.getListUserBooked();
    }

    @Override
    public int countAll() {
        return bookingRepository.countAll();
    }

    @Override
    public int countUser() {
        return bookingRepository.countUser();
    }

    @Override
    public List<StatDTO> getTurnoversByMonth() {
        return bookingRepository.getTurnoversByMonth();
    }
}
