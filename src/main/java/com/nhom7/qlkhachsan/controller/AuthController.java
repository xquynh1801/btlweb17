package com.nhom7.qlkhachsan.controller;

import com.nhom7.qlkhachsan.dto.request.RegisterRequest;
import com.nhom7.qlkhachsan.dto.ValidateMailCodeForm;
import com.nhom7.qlkhachsan.entity.mailcode.MailCode;
import com.nhom7.qlkhachsan.entity.user.Role;
import com.nhom7.qlkhachsan.entity.user.User;
import com.nhom7.qlkhachsan.repository.RoleRepository;
import com.nhom7.qlkhachsan.repository.UserRepository;
import com.nhom7.qlkhachsan.security.CustomUserDetaisService;
//import com.nhom7.qlkhachsan.utils.JwtTokenUtil;
import com.nhom7.qlkhachsan.service.MailCodeService;
import com.nhom7.qlkhachsan.service.MailService;
import com.nhom7.qlkhachsan.utils.JwtTokenUtil;
import com.nhom7.qlkhachsan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/", produces = "application/json")
public class AuthController {

    private JwtTokenUtil jwtTokenUtil;
    public HashMap<String, Integer> authenCode = new HashMap<>();
    public HashMap<String, Integer> forgotPasswordCode = new HashMap<>();
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomUserDetaisService customUserDetaisService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailCodeService mailCodeService;

    @Value("${countdown.timer.duration}")
    private long countdownDuration;

    @PostMapping(value = "/check")
    @ResponseBody
    public String validateCode(@RequestBody ValidateMailCodeForm validateMailCodeForm){
        Date date = new Date();
        User user = userService.findByFullName(validateMailCodeForm.getMail());
        System.out.println("==============>user: " + user.getUsername());

        MailCode mailCode = mailCodeService.findByMail(validateMailCodeForm.getMail());

        // Chuyển đổi Date thành LocalDateTime
        LocalDateTime localDateTime1 = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime localDateTime2 = LocalDateTime.ofInstant(mailCode.getCreatedAt().toInstant(), ZoneId.systemDefault());

        // Tính chênh lệch thời gian bằng Duration
        Duration duration = Duration.between(localDateTime1, localDateTime2);

        long seconds = duration.getSeconds(); // Tổng số giây
        System.out.println("==============>seconds: " + seconds);
        long absoluteValue = Math.abs(seconds);
        String res = "";
        if(absoluteValue < 120 && validateMailCodeForm.getCode()==mailCode.getCode()){
            res = "thanh cong";
        }
        else if (absoluteValue < 120 && validateMailCodeForm.getCode()!=mailCode.getCode()) {
            res = "nhap sai code -> that bai";
        }
        else{
            userRepository.delete(user);
            res = "het han code -> that bai";
        }
        return res;
    }

    private void startCountdownTimer(String recipientEmail, int code) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.schedule(() -> {
            // Countdown timer logic
            // ...

            System.out.println("Countdown timer finished for " + recipientEmail + ", code: " + code);
        }, countdownDuration, TimeUnit.MILLISECONDS);
    }

    @PostMapping(value = "/register")
    @ResponseBody
    public String registerUser(@RequestBody RegisterRequest registerRequest){
        try {
            String res = "email da ton tai";
            User user = userService.findByFullName(registerRequest.getFullName());
            System.out.println("======97=====>check mail " + user.getFullName());
            if (user!=null){
                System.out.println("======99=====>email da ton tai " + user.getFullName());
                return res;
            }
        }catch (Exception e){
            // todo
        }
        try{
            Random generator = new Random();
            User user = new User();

            user.setUsername(registerRequest.getUsername());
            user.setPassword(new BCryptPasswordEncoder().encode(registerRequest.getPassword()));
            user.setFullName(registerRequest.getFullName());
            user.setAge(registerRequest.getAge());
            user.setPhoneNumber(registerRequest.getPhoneNumber());
            user.setIdentityCardNumber(registerRequest.getIdentityCardNumber());

            Role role = roleRepository.findByRoleName("ROLE_USER");
            String res = "thanh cong";
            HashSet roles = new HashSet();
            roles.add(role);
            user.setRoles(roles);
            userService.createUser(user);

            int code = generator.nextInt((9999 - 1000) + 1) + 1000;
            authenCode.put(user.getFullName(), code);

//            new Gmailer().sendMail(user.getFullName(), "Xác thực email", GmailForm.mailForm(user.getUsername(), code));
            mailService.sendMail(user.getUsername(), user.getFullName(), code);

            MailCode mailCode = new MailCode();
            mailCode.setMail(registerRequest.getFullName());
            mailCode.setCode(code);
            mailCode.setCreatedAt(new Date());
            mailCodeService.createMailCode(mailCode);

//            startCountdownTimer(loginDTO.getFullName(), code);
            System.out.println("code: "+ authenCode.get(user.getFullName()).toString());
            return res;
        }catch (Exception e){
            String res = "that bai";
            return res;
        }
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
