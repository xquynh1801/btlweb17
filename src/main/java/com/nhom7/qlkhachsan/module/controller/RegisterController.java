package com.nhom7.qlkhachsan.module.controller;

import com.nhom7.qlkhachsan.app.entity.dto.response.ObjectResponse;
import com.nhom7.qlkhachsan.module.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RegisterController {

    private final UserService userService;

    @PostMapping("/register")
    public ObjectResponse register(HttpServletRequest request){
        try{
            userService.registerUser(request);
            return new ObjectResponse(HttpStatus.OK.value(), "Verify OTP in email");
        }catch (Exception e){
            log.error(e.getMessage());
            return new ObjectResponse(HttpStatus.BAD_REQUEST.value(), "Can't register, some thing wrong");
        }
    }

    @PostMapping(value = "/checkRegister/{secretKey}/{otp}")
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
