package com.nhom7.qlkhachsan.controller;

import com.nhom7.qlkhachsan.dto.ObjectResponse;
import com.nhom7.qlkhachsan.entity.UserMessage;
import com.nhom7.qlkhachsan.entity.user.User;
import com.nhom7.qlkhachsan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RegisterController {

    @Value("${spring.mail.username}")
    private String mailServer;
    @Value("${web.login.url}")
    private String loginPath;



    private final UserService userService;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @PostMapping("/register")
    public String register(@RequestBody UserMessage userMessage){
        try{
            String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));

            userMessage.setOtpSignUp(otp);
            userService.saveToRedis(userMessage);
            sendConfirmLink(userMessage);
            return "Confirm email";
        }catch (Exception e){
            log.error(e.getMessage());
            return "Can't register, some thing wrong";
        }
    }

    public void sendConfirmLink(UserMessage user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        String linkConfirm = String.format("%s/confirm-user/%s", "http://localhost:8080", user.getEmail());
        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);

        helper.setFrom(mailServer);
        helper.setTo(user.getEmail());
        helper.setSubject("Please confirm your account");
        String html = templateEngine.process("confirm-email.html", context);
        helper.setText(html, true);

        mailSender.send(message);
    }

    @GetMapping(path = "/confirm-user/{secretKey}/{otp}")
    public ObjectResponse confirmUser(@PathVariable String secretKey, @PathVariable String otp) throws IOException {

        try {
            UserMessage user = userService.confirmSecretKey(secretKey);
            if(otp.equals(user.getOtpSignUp())) {
                log.info("Verify success, save user to DB");
                userService.createUser(User.builder()
                                .username(user.getEmail())
                                .fullName(user.getFullName())
                                .age(user.getAge())
                                .password(user.getPassword())
                                .identityCardNumber(user.getIdentityCardNumber())
                                .phoneNumber(user.getPhoneNumber())
                        .build());
                return new ObjectResponse(HttpStatus.BAD_REQUEST.value(), "Verify success");
            }
            else{
                log.info("Not match");
                return new ObjectResponse(HttpStatus.BAD_REQUEST.value(), "OTP not match");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ObjectResponse(HttpStatus.BAD_REQUEST.value(), "Verify error");
        }
    }

    @GetMapping(path = "/get-all-user-redis")
    public Object confirmUser()  {
        try {
            return userService.getUserInRedis();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }
}
