package com.nhom7.qlkhachsan.controller;

import com.nhom7.qlkhachsan.dto.request.RegisterRequest;
import com.nhom7.qlkhachsan.dto.response.ObjectResponse;
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RegisterController {

    private final UserService userService;

    @PostMapping("/register")
    public ObjectResponse register(@RequestBody RegisterRequest request){
        try{
            userService.registerUser(request);
            return new ObjectResponse(HttpStatus.OK.value(), "Verify OTP in email");
        }catch (Exception e){
            log.error(e.getMessage());
            return new ObjectResponse(HttpStatus.BAD_REQUEST.value(), "Can't register, some thing wrong");
        }
    }

    @GetMapping(path = "/confirm-user/{secretKey}/{otp}")
    public ObjectResponse confirmUser(@PathVariable String secretKey, @PathVariable String otp) throws IOException {
        try {
            userService.confirmSecretKey(secretKey, otp);
            return new ObjectResponse(HttpStatus.OK.value(), "Verify success");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ObjectResponse(HttpStatus.BAD_REQUEST.value(), "Verify failed");
        }
    }

    @GetMapping(path = "/get-all-user-redis")
    public ObjectResponse confirmUser()  {
        try {
            return new ObjectResponse(HttpStatus.OK.value(), "users", userService.getUserInRedis());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ObjectResponse(HttpStatus.OK.value(), "users", e.getMessage());
        }
    }
}
