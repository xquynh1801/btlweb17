package com.nhom7.qlkhachsan.app.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ValidateMailCodeForm implements Serializable {
    private String mail;
    private int code;
}
