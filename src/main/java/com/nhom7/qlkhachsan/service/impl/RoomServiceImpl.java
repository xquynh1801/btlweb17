package com.nhom7.qlkhachsan.service.impl;

import com.nhom7.qlkhachsan.entity.hotel.Room;
import com.nhom7.qlkhachsan.repository.RoomRepository;
import com.nhom7.qlkhachsan.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Override
    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public List<Room> getEmptyRooms(Long idHotel) {
        return roomRepository.findAllByHotelHasRoomsIdAndStatus(idHotel, true);
    }

     @Override
    public List<Room> getAllRooms(Long id) {
        return roomRepository.getAllByHotelHasRoomsId(id);
    }

    @Override
    public Room findById(Long id) {
        return roomRepository.getById(id);
    }

    @Override
    public void save(Room room) {
        roomRepository.save(room);
    }

    @Override
    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }


}
