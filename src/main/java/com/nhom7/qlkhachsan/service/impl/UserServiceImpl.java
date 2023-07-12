package com.nhom7.qlkhachsan.service.impl;

import com.nhom7.qlkhachsan.dto.LoginDTO;
import com.nhom7.qlkhachsan.dto.UserDTO;
import com.nhom7.qlkhachsan.entity.user.Role;
import com.nhom7.qlkhachsan.entity.user.User;
import com.nhom7.qlkhachsan.repository.UserRepository;
import com.nhom7.qlkhachsan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

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
    public UserDTO confirmSecretKey(String secretKey) {
        //get from redis
        return (UserDTO) redisTemplate.opsForValue().get(secretKey);
        // save to db
    }

    @Override
    public UserDTO saveToRedis(UserDTO userDTO){
//        redisTemplate.opsForHash().put("User_register", userMessage.getUserId(), userMessage);
        redisTemplate.opsForValue().set(userDTO.getFullName(),userDTO, Duration.ofSeconds(120));
        System.out.println(userDTO);
        return userDTO;
    }

    public List<UserDTO> getUserInRedis(){
        return redisTemplate.opsForHash().values("User_register");
    }
}
