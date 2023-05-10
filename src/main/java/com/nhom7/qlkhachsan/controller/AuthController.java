package com.nhom7.qlkhachsan.controller;

import com.nhom7.qlkhachsan.entity.user.Role;
import com.nhom7.qlkhachsan.entity.user.User;
import com.nhom7.qlkhachsan.repository.HotelRepository;
import com.nhom7.qlkhachsan.repository.RoleRepository;
import com.nhom7.qlkhachsan.security.CustomUserDetaisService;
import com.nhom7.qlkhachsan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/")
public class AuthController {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomUserDetaisService customUserDetaisService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/signup")
    public String registerUser(User user){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        Role role = roleRepository.findByRoleName("ROLE_USER");
        HashSet roles = new HashSet();
        roles.add(role);
        user.setRoles(roles);
        userService.createUser(user);
        return "login";
    }

    @GetMapping("/signup")
    String register(Model model){
        model.addAttribute("user", new User());
        return "signup";
    }

    @GetMapping("/logout")
    public String logout(){
        return "login";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/subcribe")
    public String userSubcribe(){
        User user = getCurrentUser();
        if (user.getRoles().size() == 1)
            return "subcribe";
        else return "redirect:/admin";
    }

    @PostMapping("/subcribe")
    public String subcribe(){
        User user = getCurrentUser();
        Set<Role> roles = user.getRoles();
        roles.add(roleRepository.findByRoleName("ROLE_MANAGER"));
        user.setRoles(roles);
        userService.createUser(user);
        return "subcribeSuccess";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userService.findByUserName(username);
    }
}
