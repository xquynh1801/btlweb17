package com.nhom7.qlkhachsan.module.service;

import com.nhom7.qlkhachsan.app.entity.dto.HotelDTO;
import com.nhom7.qlkhachsan.app.entity.hotel.Hotel;
import com.nhom7.qlkhachsan.app.entity.hotel.Room;
import com.nhom7.qlkhachsan.app.entity.rating.Comment;
import com.nhom7.qlkhachsan.app.entity.rating.Follow;
import com.nhom7.qlkhachsan.app.entity.user.User;

import java.util.List;

public interface HotelService {
    List<HotelDTO> getAll();

    List<Hotel> searchHotelByName(String nameHotel);

    Hotel getHotelByName(String nameHotel);

    HotelDTO getHotelByID(Long id);
    
    List<Hotel> getHotelsByOwner(User user);

    List<Room> getAllByHotelID(Long id);

    List<Room> getSomeRoomInType(Long idHotel);

    Hotel addHotel(Hotel hotel);

    Follow getFollowByUserAndHotel(Long userId, Long hotelId);

    void followHotel(User user, Long hotelID);

    void unfollowHotel(User user, Long hotelID);

    void commentHotel(User user, Long hotelID);

    List<Comment> getCommentByUserAndHotel(Long userId, Long hotelId);

    Comment getLastestComment(Long hotelId);
    
    void deleteById(Long id);

    void updateHotel(Hotel hotel);
}
