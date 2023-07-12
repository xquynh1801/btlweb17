package com.nhom7.qlkhachsan.dto;

import com.nhom7.qlkhachsan.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    private String username;
    private String password;
    private String fullName;
    private Integer age;
    private String phoneNumber;
    private String identityCardNumber;
}
