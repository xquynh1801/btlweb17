package com.nhom7.qlkhachsan.service;

import com.nhom7.qlkhachsan.dto.request.RegisterRequest;
import com.nhom7.qlkhachsan.entity.user.User;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    User createUser(User user);

    User findByUserName(String username);

    User findByFullName(String fullName);

    List<User> getAll();

    void saveToRedis(RegisterRequest userDTO);

    void confirmSecretKey(String secretKey, String otp);

    void confirmSecretKeyLogin(String mail, String otp);

    List<User> getUserInRedis();

    void registerUser(HttpServletRequest request) throws MessagingException;
    void loginUser(String username, String password) throws MessagingException;
}
