package com.nhom7.qlkhachsan.module.controller;

import com.nhom7.qlkhachsan.app.entity.dto.response.ObjectResponse;
import com.nhom7.qlkhachsan.module.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final UserService userService;

    @PostMapping("/login/{username}/{password}")
    public ObjectResponse login(@PathVariable String username, @PathVariable String password){
        try{
            userService.loginUser(username, password);
            return new ObjectResponse(HttpStatus.OK.value(), "Verify OTP in email");
        }catch (Exception e){
            log.error(e.getMessage());
            return new ObjectResponse(HttpStatus.BAD_REQUEST.value(), "Can't login, some thing wrong");
        }
    }

    @PostMapping(value = "/checkLogin/{mail}/{otp}")
    public ObjectResponse confirmUserLogin(@PathVariable String mail, @PathVariable String otp) throws IOException {
        try {
            userService.confirmSecretKeyLogin(mail, otp);
            return new ObjectResponse(HttpStatus.OK.value(), "Verify success");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ObjectResponse(HttpStatus.BAD_REQUEST.value(), "Verify failed");
        }
    }
}
