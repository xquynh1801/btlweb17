package com.nhom7.qlkhachsan.module.controller;

import com.nhom7.qlkhachsan.app.entity.user.User;
import com.nhom7.qlkhachsan.app.database.repository.BookingRepository;
import com.nhom7.qlkhachsan.app.database.repository.RoleRepository;
import com.nhom7.qlkhachsan.app.database.repository.UserRepository;
import com.nhom7.qlkhachsan.module.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
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
    public ResponseEntity<?> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAll());
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
