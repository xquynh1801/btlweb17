package com.nhom7.qlkhachsan.controller;

import com.nhom7.qlkhachsan.base.Error;
import com.nhom7.qlkhachsan.base.ObjectResponse;
import com.nhom7.qlkhachsan.dto.LoginDTO;
import com.nhom7.qlkhachsan.dto.UserDTO;
import com.nhom7.qlkhachsan.dto.ValidateMailCodeForm;
import com.nhom7.qlkhachsan.entity.mailcode.MailCode;
import com.nhom7.qlkhachsan.entity.user.Role;
import com.nhom7.qlkhachsan.entity.user.User;
import com.nhom7.qlkhachsan.gmail.GmailForm;
import com.nhom7.qlkhachsan.gmail.Gmailer;
import com.nhom7.qlkhachsan.repository.MailCodeRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    private MailCodeRepository mailCodeRepository;
    @Autowired
    private MailCodeService mailCodeService;

    @Value("${countdown.timer.duration}")
    private long countdownDuration;

    @PostMapping(value = "/check")
    @ResponseBody
    public ObjectResponse validateCode(@RequestBody ValidateMailCodeForm validateMailCodeForm){
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
        if(absoluteValue < 120 && validateMailCodeForm.getCode()==mailCode.getCode()){
            return new ObjectResponse(Error.OK.getErrorCode(), Error.OK.getMessage(), null);
        }
        else if (absoluteValue < 120 && validateMailCodeForm.getCode()!=mailCode.getCode()) {
            return new ObjectResponse(Error.NOT_OK.getErrorCode(), Error.NOT_OK.getMessage(), null);
        }
        else{
            userRepository.delete(user);
            mailCodeRepository.delete(mailCode);
            return new ObjectResponse(Error.NOT_OK.getErrorCode(), Error.NOT_OK.getMessage(), null);
        }
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
    public ObjectResponse registerUser(HttpServletRequest request){
        LoginDTO loginDTO = (LoginDTO) request.getAttribute("loginDTO");
        System.out.println("==========requestEmail: "+loginDTO.getFullName());
        try {
            User user = userService.findByFullName(loginDTO.getFullName());
            System.out.println("======97=====>check mail " + user.getFullName());
            if (user!=null){
                System.out.println("======99=====>email da ton tai " + user.getFullName());
                return new ObjectResponse(Error.NOT_OK.getErrorCode(), Error.NOT_OK.getMessage(), null);
            }
        }catch (Exception e){
            // todo
        }
        try{
            Random generator = new Random();
//            User user = new User();
//
//            user.setUsername(loginDTO.getUsername());
//            user.setPassword(new BCryptPasswordEncoder().encode(loginDTO.getPassword()));
//            user.setFullName(loginDTO.getFullName());
//            user.setAge(loginDTO.getAge());
//            user.setPhoneNumber(loginDTO.getPhoneNumber());
//            user.setIdentityCardNumber(loginDTO.getIdentityCardNumber());
//
//            Role role = roleRepository.findByRoleName("ROLE_USER");
//            HashSet roles = new HashSet();
//            roles.add(role);
//            user.setRoles(roles);
//            userService.createUser(user);

            int code = generator.nextInt((9999 - 1000) + 1) + 1000;
            UserDTO userDTO = new UserDTO();

            userDTO.setUsername(loginDTO.getUsername());
            userDTO.setPassword(new BCryptPasswordEncoder().encode(loginDTO.getPassword()));
            userDTO.setOtpSignUp(String.valueOf(code));
            userDTO.setFullName(loginDTO.getFullName());
            userDTO.setAge(loginDTO.getAge());
            userDTO.setPhoneNumber(loginDTO.getPhoneNumber());
            userDTO.setIdentityCardNumber(loginDTO.getIdentityCardNumber());

            Role role = roleRepository.findByRoleName("ROLE_USER");
            HashSet roles = new HashSet();
            roles.add(role);
//            user.setRoles(roles);
            userService.saveToRedis(userDTO);
            authenCode.put(userDTO.getFullName(), code);
            mailService.sendMail(userDTO.getUsername(), userDTO.getFullName(), code);

//            MailCode mailCode = new MailCode();
//            mailCode.setMail(loginDTO.getFullName());
//            mailCode.setCode(code);
//            mailCode.setCreatedAt(new Date());
//            mailCodeService.createMailCode(mailCode);

            System.out.println("code: "+ authenCode.get(userDTO.getFullName()).toString());
            return new ObjectResponse(Error.OK.getErrorCode(), Error.OK.getMessage(), null);
        }catch (Exception e){
            return new ObjectResponse(Error.NOT_OK.getErrorCode(), Error.NOT_OK.getMessage(), null);
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
