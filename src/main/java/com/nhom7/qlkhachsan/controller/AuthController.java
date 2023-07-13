package com.nhom7.qlkhachsan.controller;

import com.nhom7.qlkhachsan.entity.user.User;
import com.nhom7.qlkhachsan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/", produces = "application/json")
public class AuthController {
    @Autowired
    private UserService userService;

    @Value("${countdown.timer.duration}")
    private long countdownDuration;

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

//    @GetMapping("/subcribe")
//    public String userSubcribe(){
//        User user = getCurrentUser();
//        if (user.getRoles().size() == 1)
//            return "subcribe";
//        else return "redirect:/admin";
//    }
//
//    @PostMapping("/subcribe")
//    public String subcribe(){
//        User user = getCurrentUser();
//        Set<Role> roles = user.getRoles();
//        roles.add(roleRepository.findByRoleName("ROLE_MANAGER"));
//        user.setRoles(roles);
//        userService.createUser(user);
//        return "subcribeSuccess";
//    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userService.findByUserName(username);
    }
}
