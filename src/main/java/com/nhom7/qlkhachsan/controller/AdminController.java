package com.nhom7.qlkhachsan.controller;

import com.nhom7.qlkhachsan.entity.hotel.BookingRoom;
import com.nhom7.qlkhachsan.entity.hotel.Hotel;
import com.nhom7.qlkhachsan.entity.hotel.Room;
import com.nhom7.qlkhachsan.entity.user.User;
import com.nhom7.qlkhachsan.repository.HotelRepository;
import com.nhom7.qlkhachsan.service.BookingRoomService;
import com.nhom7.qlkhachsan.service.HotelService;
import com.nhom7.qlkhachsan.service.RoomService;
import com.nhom7.qlkhachsan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingRoomService bookingRoomService;
    
    @GetMapping
    public String home(Model model) {

        // count current room
        List<Hotel> list = hotelService.getHotelsByOwner(getCurrentUser());
        int rooms = 0;
        for (int i = 0; i < list.size(); i++) {
            rooms += roomService.getAllRooms(list.get(i).getId()).size();
        }
        model.addAttribute("rooms", rooms);

        // count current user
        int users = bookingRoomService.countUser();
        model.addAttribute("users", users);

        // count current reservation
        int reservations = bookingRoomService.countAll();
        model.addAttribute("reservations", reservations);
        model.addAttribute("loginedUser", getCurrentUser());
        return "/admin/index";
    }

    @GetMapping("addRoom")
    public String loadPageAddRoom(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("loginedUser", getCurrentUser());
        model.addAttribute("hotels", hotelRepository.findAllByOwner(getCurrentUser()));
        return "/admin/rooms/add_room";
    }
    
    @GetMapping("addHotel")
    public String loadPageAddHotel(Model model) {
        model.addAttribute("hotel", new Hotel());
        model.addAttribute("loginedUser", getCurrentUser());
        return "/admin/rooms/addHotel";
    }

    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    @PostMapping("/addHotel")
    public String addHotel(Hotel hotel, @RequestParam("file") MultipartFile image) throws IOException {
        Path staticPath = Paths.get("QLKhachsan/src/main/resources/static/media/images/hotel_and_room_images");
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath).resolve(image.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }
        hotel.setImagePath(staticPath.resolve(image.getOriginalFilename()).toString().substring(37));
        hotel.setOwner(getCurrentUser());
        hotelService.addHotel(hotel);
        return "redirect:/admin";
    }

    @PostMapping("/addRoom")
    public String addRoom(Room room, @RequestParam("file") MultipartFile image) throws IOException {
        Path staticPath = Paths.get("QLKhachsan/src/main/resources/static/media/images/hotel_and_room_images");
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath).resolve(image.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }
        room.setImagePath(staticPath.resolve(image.getOriginalFilename()).toString().substring(37));
        room.setStatus(true);
        roomService.addRoom(room);
        return "redirect:/admin";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userService.findByUserName(username);
    }
     @GetMapping("/hotels")
    public String listHotel(Model model) {
        List<Hotel> hotels = hotelRepository.findAllByOwner(getCurrentUser());
         model.addAttribute("loginedUser", getCurrentUser());
        model.addAttribute("hotels", hotels);
        return "/admin/rooms/showHotel";
    }

    @GetMapping("/editHotel/{id}")
    public String editHotel(@PathVariable Long id, Model model ) {
        model.addAttribute("hotel", hotelService.getHotelByID(id));
        model.addAttribute("loginedUser", getCurrentUser());
        return "/admin/rooms/editHotel";
    }
    @PostMapping("/editHotel/{id}")
    public String editHotel(@PathVariable Long id,Hotel hotel, @RequestParam("file") MultipartFile image) throws IOException {
        Path staticPath = Paths.get("QLKhachsan/src/main/resources/static/media/images/hotel_and_room_images");
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath).resolve(image.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }
        hotel.setId(id);
        hotel.setImagePath(staticPath.resolve(image.getOriginalFilename()).toString().substring(37));
        hotel.setOwner(getCurrentUser());
        hotelService.updateHotel(hotel);
        return "redirect:/admin/hotels";
    }

   @GetMapping("/deleteHotel/{id}")
    public String deleteHotel(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        if (roomService.getAllRooms(id) == null) {
            model.addAttribute("id", id);
            model.addAttribute("loginedUser", getCurrentUser());
            return "/admin/rooms/deleteHotel";
        }
        else {
            redirectAttributes.addFlashAttribute("message", "Không thể xóa khách sạn do tồn tại phòng");
            return "redirect:/admin/hotels";
        }
    }

    @PostMapping("/deleteHotel/{id}")
    public String deleteHotel(@PathVariable Long id) {
        hotelService.deleteById(id);
        return "redirect:/admin/hotels";
    }
    @GetMapping("/listRooms")
    public String listRoom(Model model) {
        model.addAttribute("hotels", hotelService.getHotelsByOwner(getCurrentUser()));
        model.addAttribute("loginedUser", getCurrentUser());
        return "/admin/rooms/listHotelChoose";
    }

    @GetMapping("/rooms/{id}")
    public String rooms(@PathVariable Long id, Model model) {
        model.addAttribute("rooms", roomService.getAllRooms(id));
        model.addAttribute("loginedUser", getCurrentUser());
        return "/admin/rooms/showrooms";
    }

    @GetMapping("/editRoom/{id}")
    public String editRoom(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.findById(id));
        model.addAttribute("loginedUser", getCurrentUser());
        return "/admin/rooms/editRoom";
    }

    @PostMapping("/editRoom/{id}")
    public String editRoom(@PathVariable Long id, Room room, @RequestParam(value = "file",required = false) MultipartFile image) throws IOException {
        Path staticPath = Paths.get("QLKhachsan/src/main/resources/static/media/images/hotel_and_room_images");
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath).resolve(image.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }
        room.setHotelHasRooms(roomService.findById(id).getHotelHasRooms());
        room.setImagePath(staticPath.resolve(image.getOriginalFilename()).toString().substring(37));
        roomService.save(room);
        return "redirect:/admin/listRooms";
    }

    @GetMapping("/deleteRoom/{id}")
    public String deleteRoom(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("loginedUser", getCurrentUser());
        return "/admin/rooms/deleteRoom";
    }

    @PostMapping("/deleteRoom/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteById(id);
        return "redirect:/admin/listRooms";
    }

     @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("bookings", bookingRoomService.getAll());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("rooms", roomService.getAll());
         model.addAttribute("loginedUser", getCurrentUser());
        return "/admin/rooms/users";
    }
     @GetMapping("/statsByMonth")
    public String stat(Model model) {
        model.addAttribute("bookings", bookingRoomService.getTurnoversByMonth());
         model.addAttribute("loginedUser", getCurrentUser());
        return "/admin/rooms/StatsByMonth";
    }
}
