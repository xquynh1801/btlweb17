package com.nhom7.qlkhachsan.module.controller;

import com.nhom7.qlkhachsan.app.entity.user.User;
import com.nhom7.qlkhachsan.app.database.repository.BookingRepository;
import com.nhom7.qlkhachsan.app.database.repository.RoleRepository;
import com.nhom7.qlkhachsan.app.database.repository.UserRepository;
import com.nhom7.qlkhachsan.module.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoleRepository roleRepository;

//    @GetMapping()
//    public String loadPageReservation(Model model) {
//        List<Hotel> hotels = hotelService.getAll();
//        model.addAttribute("hotels", hotels);
//        return "showHotels";
//    }


//    @GetMapping("/hotel={name}")
//    public String getUIbookingRoom(@PathVariable String name, Model model){
//        Hotel hotel = hotelService.getHotelByName(name);
//        List<Room> rooms = hotelService.getAllByHotelID(hotel.getId());
//        model.addAttribute("hotel", hotel);
//        model.addAttribute("rooms", rooms);
//        model.addAttribute("booking", new BookingRoom());
//        return "reservation";
//    }

//    @PostMapping("/hotel={name}")
//    public String bookingRoom(@PathVariable String name, BookingRoom bookingRoom){
//        bookingRoom.setUserBook(getCurrentUser());
//        bookingRepository.save(bookingRoom);
//        return "bookingSuccessful";
//    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username);
    }
}
