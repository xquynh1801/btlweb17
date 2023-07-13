package com.nhom7.qlkhachsan.service.impl;

import com.nhom7.qlkhachsan.dto.LoginDTO;
import com.nhom7.qlkhachsan.dto.request.RegisterRequest;
import com.nhom7.qlkhachsan.entity.user.Role;
import com.nhom7.qlkhachsan.entity.user.User;
import com.nhom7.qlkhachsan.repository.RoleRepository;
import com.nhom7.qlkhachsan.repository.UserRepository;
import com.nhom7.qlkhachsan.service.MailService;
import com.nhom7.qlkhachsan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.nhom7.qlkhachsan.security.SecurityConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Value("${spring.mail.username}")
    private String mailServer;
    @Value("${web.login.url}")
    private String loginPath;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Autowired
    private MailService mailService;

    @Override
    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByFullName(String fullName) {
        return userRepository.findByFullName(fullName);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void confirmSecretKey(String secretKey, String otp) {
        RegisterRequest userInRedis = (RegisterRequest) redisTemplate.opsForValue().get(secretKey);
        System.out.println("===================>userInRedis: " + userInRedis);
        if(userInRedis == null){
            log.error("OTP expired");
        }
        if(otp.equals(userInRedis.getOtpSignUp())) {
            log.info("Verify success, save user to DB");
            User user = User.builder()
                    .username(userInRedis.getUsername())
                    .fullName(userInRedis.getFullName())
                    .age(userInRedis.getAge())
                    .password(userInRedis.getPassword())
                    .identityCardNumber(userInRedis.getIdentityCardNumber())
                    .phoneNumber(userInRedis.getPhoneNumber())
                    .build();

            Role role = roleRepository.findByRoleName("ROLE_USER");
            HashSet roles = new HashSet();
            roles.add(role);
            user.setRoles(roles);
            createUser(user);
        }else{
            log.error("OTP not match");
        }
    }

    public void saveToRedis(RegisterRequest request){
        redisTemplate.opsForValue().set(request.getFullName(),request, Duration.ofMinutes(2));
        System.out.println(request);
    }

    public List<User> getUserInRedis(){
        return redisTemplate.opsForHash().values("User_register");
    }

    @Override
    public void registerUser(HttpServletRequest request) throws MessagingException {
        LoginDTO loginDTO = (LoginDTO) request.getAttribute("loginDTO");
        User user = findByFullName(loginDTO.getFullName());
        System.out.println("==========requestEmail: " + loginDTO.getFullName());
        if (user != null) {
            log.error("email existed");
        } else {
            Random generator = new Random();
            int otp = generator.nextInt((9999 - 1000) + 1) + 1000;

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername(loginDTO.getUsername());
            registerRequest.setPassword(new BCryptPasswordEncoder().encode(loginDTO.getPassword()));
            registerRequest.setFullName(loginDTO.getFullName());
            registerRequest.setAge(loginDTO.getAge());
            registerRequest.setPhoneNumber(loginDTO.getPhoneNumber());
            registerRequest.setIdentityCardNumber(loginDTO.getIdentityCardNumber());
            registerRequest.setOtpSignUp(String.valueOf(otp));

            saveToRedis(registerRequest);
            System.out.println("===================>saveToRedis: " + registerRequest);
            mailService.sendMail(registerRequest.getUsername(), registerRequest.getFullName(), otp);
            System.out.println("code: " + otp);
        }
    }


    @Override
    public void confirmSecretKeyLogin(String mail, String otp) {
        RegisterRequest userInRedis = (RegisterRequest) redisTemplate.opsForValue().get(mail);
        System.out.println("===================>userInRedis: " + userInRedis);
        if(userInRedis == null){
            log.error("OTP expired");
        }
        if(otp.equals(userInRedis.getOtpSignUp())) {
            log.info("Verify success, login");
            /////////***********************************//////////LOGIN
        }else{
            log.error("OTP not match");
        }
    }

    public void loginUser(String username, String password) throws MessagingException {
        User user = findByUserName(username);
        if (user == null) {
            log.error("user not existed");
        } else if (user != null && !(passwordEncoder.matches(password, user.getPassword()))) {
            log.error("sai mat khau");
        } else {
            Random generator = new Random();
            int otp = generator.nextInt((9999 - 1000) + 1) + 1000;

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername(user.getUsername());
            registerRequest.setPassword(user.getPassword());
            registerRequest.setFullName(user.getFullName());
            registerRequest.setAge(user.getAge());
            registerRequest.setPhoneNumber(user.getPhoneNumber());
            registerRequest.setIdentityCardNumber(user.getIdentityCardNumber());
            registerRequest.setOtpSignUp(String.valueOf(otp));

            saveToRedis(registerRequest);
            System.out.println("===================>saveLoginUserToRedis: " + registerRequest);
            mailService.sendMail(registerRequest.getUsername(), registerRequest.getFullName(), otp);
            System.out.println("code: " + otp);
        }
    }

    // use when send verify link
//    public void sendConfirmLink(RegisterRequest user) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
//        Context context = new Context();
//        String linkConfirm = String.format("%s/confirm-user/%s", "http://localhost:8080", user.getUsername());
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("linkConfirm", linkConfirm);
//        context.setVariables(properties);
//
//        helper.setFrom(mailServer);
//        helper.setTo(user.getUsername());
//        helper.setSubject("Please confirm your account");
//        String html = templateEngine.process("confirm-email.html", context);
//        helper.setText(html, true);
//
//        mailSender.send(message);
//    }
}
