package com.nhom7.qlkhachsan.service;

import com.nhom7.qlkhachsan.entity.UserMessage;
import com.nhom7.qlkhachsan.entity.user.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User findByUserName(String username);

    List<User> getAll();

    UserMessage confirmSecretKey(String secretKey);

    UserMessage saveToRedis(UserMessage userMessage);

    List<UserMessage> getUserInRedis();
}
