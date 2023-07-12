package com.nhom7.qlkhachsan.service;

import com.nhom7.qlkhachsan.dto.LoginDTO;
import com.nhom7.qlkhachsan.dto.UserDTO;
import com.nhom7.qlkhachsan.entity.user.Role;
import com.nhom7.qlkhachsan.entity.user.User;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {
    User createUser(User user);

    User findByUserName(String username);

    User findByFullName(String fullName);

    List<User> getAll();
    UserDTO confirmSecretKey(String secretKey);

    UserDTO saveToRedis(UserDTO userDTO);

    List<UserDTO> getUserInRedis();

    void confirmSecretKey(String secretKey, String otp);

    List<User> getUserInRedis();

    void registerUser(RegisterRequest request) throws MessagingException;
}
