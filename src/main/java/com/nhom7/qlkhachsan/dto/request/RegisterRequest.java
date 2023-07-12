package com.nhom7.qlkhachsan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import javax.persistence.Id;
import java.io.Serializable;

@RedisHash(value = "User_register")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EnableRedisRepositories
public class RegisterRequest implements Serializable {
    @Id
    private String username;
    private String password;
    private String fullName;
    private Integer age;
    private String phoneNumber;
    private String identityCardNumber;
    private String otpSignUp;
}
