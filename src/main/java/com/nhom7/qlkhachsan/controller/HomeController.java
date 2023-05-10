package com.nhom7.qlkhachsan.controller;

import com.nhom7.qlkhachsan.dto.BookingDTO;
import com.nhom7.qlkhachsan.dto.RoleDTO;
import com.nhom7.qlkhachsan.dto.SearchingDTO;
import com.nhom7.qlkhachsan.entity.hotel.BookingRoom;
import com.nhom7.qlkhachsan.entity.hotel.Hotel;
import com.nhom7.qlkhachsan.entity.user.User;
import com.nhom7.qlkhachsan.repository.BookingRepository;
import com.nhom7.qlkhachsan.repository.RoleRepository;
import com.nhom7.qlkhachsan.repository.UserRepository;
import com.nhom7.qlkhachsan.service.BookingRoomService;
import com.nhom7.qlkhachsan.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("loginedUser", getCurrentUser());
        if(getCurrentUser()!=null){
            List<RoleDTO> roles = roleRepository.getRolesByUserId(getCurrentUser().getId());
            for(RoleDTO r:roles){
                model.addAttribute(r.getName(), r.getName());
            }
        }
        List<Hotel> hotels = hotelService.getAll();
        model.addAttribute("hotels",hotels);
        return "index";
    }

    @GetMapping("/my_reservation")
    public String users(Model model) {
        model.addAttribute("loginedUser", getCurrentUser());
        model.addAttribute("bookings", bookingRepository.getAllByUserBookId(getCurrentUser().getId()));
        return "myReservation";
    }


    @GetMapping("/rooms")
    public String loadPageRooms() {
        return "rooms";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username);
    }

}
