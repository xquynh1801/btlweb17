package com.nhom7.qlkhachsan.repository;

import com.nhom7.qlkhachsan.entity.hotel.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query(value = "select * from room r where r.hotel_id = id", nativeQuery = true)
    List<Room> getAllRoomsByHotelID(Long id);

    List<Room> getAllByHotelHasRoomsId(Long hotelId);
    List<Room> findAllByHotelHasRoomsIdAndStatus(Long hotelId, boolean status);
    Room findByRoomName(String roomName);

    Room findByRoomNameAndHotelHasRoomsId(String roomName, Long hotelId);

    Room getById(Long id);

    Room save(Room room);

    void deleteById(Long id);
}
