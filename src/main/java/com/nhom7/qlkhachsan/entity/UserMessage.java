package com.nhom7.qlkhachsan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@RedisHash(value = "User_register", timeToLive = 10)
@NoArgsConstructor
@AllArgsConstructor
@Data
@EnableRedisRepositories
public class UserMessage implements Serializable {
    @Id
    private String email;
    private String password;
    private String otpSignUp;
    private String fullName;
    private Integer age;
    private String phoneNumber;
    private String identityCardNumber;
}
