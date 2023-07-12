package com.nhom7.qlkhachsan.service;

import com.nhom7.qlkhachsan.dto.request.RegisterRequest;
import com.nhom7.qlkhachsan.entity.user.User;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {
    User createUser(User user);

    User findByUserName(String username);

    User findByFullName(String fullName);

    List<User> getAll();

    void saveToRedis(RegisterRequest userDTO);

    void confirmSecretKey(String secretKey, String otp);

    List<User> getUserInRedis();

    void registerUser(RegisterRequest request) throws MessagingException;
}
