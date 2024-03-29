package com.nhom7.qlkhachsan.module.service.impl;

import com.nhom7.qlkhachsan.app.database.repository.*;
import com.nhom7.qlkhachsan.app.entity.dto.HotelDTO;
import com.nhom7.qlkhachsan.app.entity.hotel.Hotel;
import com.nhom7.qlkhachsan.app.entity.hotel.Room;
import com.nhom7.qlkhachsan.app.entity.rating.Comment;
import com.nhom7.qlkhachsan.app.entity.rating.Follow;
import com.nhom7.qlkhachsan.app.entity.user.User;
import com.nhom7.qlkhachsan.module.service.HotelService;
import com.nhom7.qlkhachsan.app.database.repository.*;
import com.nhom7.qlkhachsan.module.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<HotelDTO> getAll() {
        return ModelMapper.hotelMapper(hotelRepository.findAll());
    }

    @Override
    public List<Hotel> searchHotelByName(String nameHotel) {
        return hotelRepository.findAllByNameContaining(nameHotel);
    }

    @Override
    public Hotel getHotelByName(String nameHotel) {
        return hotelRepository.findByName(nameHotel);
    }

    @Override
    public HotelDTO getHotelByID(Long id) {
        return ModelMapper.hotelMapper1(hotelRepository.getById(id));
    }

    @Override
    public List<Hotel> getHotelsByOwner(User user) {
        return hotelRepository.findAllByOwner(user);
    }

    @Override
    public List<Room> getAllByHotelID(Long id) {
        return roomRepository.getAllRoomsByHotelID(id);
    }

    @Override
    public List<Room> getSomeRoomInType(Long idHotel) {
        return roomRepository.getAllByHotelHasRoomsId(idHotel);
    }

    @Override
    public Hotel addHotel(Hotel hotel){
        return hotelRepository.save(hotel);
    }

    @Override
    public Follow getFollowByUserAndHotel(Long userId, Long hotelId) {
        return followRepository.findByHotelIsFollowedIdAndUserFollowId(hotelId, userId);
    }

    @Override
    public void followHotel(User user, Long hotelID) {
        Follow follow = new Follow();
        Hotel hotel = hotelRepository.getById(hotelID);
        follow.setUserFollow(user);
        follow.setHotelIsFollowed(hotel);
        followRepository.save(follow);
        System.out.println("Follow successfully");
    }

    @Override
    public void unfollowHotel(User user, Long hotelID) {
        Follow follow = followRepository.findByHotelIsFollowedIdAndUserFollowId(hotelID, user.getId());
        followRepository.delete(follow);
        System.out.println("Unfollow successfully");
    }

    @Override
    public void commentHotel(User user, Long hotelID) {
        Comment comment = new Comment();
        Hotel hotel = hotelRepository.getById(hotelID);
        comment.setUserComment(user);
        comment.setHotelIsCommented(hotel);
        commentRepository.save(new Comment());
        System.out.println("Comment successfully");
    }

    @Override
    public List<Comment> getCommentByUserAndHotel(Long userId, Long hotelId) {
        return commentRepository.findByUserCommentIdAndHotelIsCommentedId(userId, hotelId);
    }

    @Override
    public Comment getLastestComment(Long hotelId) {
        return commentRepository.findByHotelIsCommentedId(hotelId);
    }

    @Override
    public void deleteById(Long id) {
        hotelRepository.deleteById(id);
    }

    @Override
    public void updateHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }
}
