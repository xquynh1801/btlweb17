package com.nhom7.qlkhachsan.service.impl;

import com.google.gson.Gson;
import com.nhom7.qlkhachsan.entity.RedisTest;
import com.nhom7.qlkhachsan.entity.UserMessage;
import com.nhom7.qlkhachsan.entity.user.Role;
import com.nhom7.qlkhachsan.entity.user.User;
import com.nhom7.qlkhachsan.repository.UserRepository;
import com.nhom7.qlkhachsan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserMessage confirmSecretKey(String secretKey) {
        //get from redis
        return (UserMessage) redisTemplate.opsForValue().get(secretKey);
        // save to db
    }

    @Override
    public UserMessage saveToRedis(UserMessage userMessage){
//        redisTemplate.opsForHash().put("User_register", userMessage.getUserId(), userMessage);
        redisTemplate.opsForValue().set(userMessage.getEmail(),userMessage, Duration.ofMinutes(20));
        System.out.println(userMessage);
        return userMessage;
    }

    public List<UserMessage> getUserInRedis(){
        return redisTemplate.opsForHash().values("User_register");
    }
}
