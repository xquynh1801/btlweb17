package com.nhom7.qlkhachsan.base;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ObjectResponse {
    private Integer httpStatus;
    private String message;
    private Object data;
}