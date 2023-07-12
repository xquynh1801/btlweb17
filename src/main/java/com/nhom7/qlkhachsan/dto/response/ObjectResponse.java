package com.nhom7.qlkhachsan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ObjectResponse {
    private Integer httpStatus;
    private String message;
    private Object data;

    public ObjectResponse(Integer httpStatus, String message){
        this.httpStatus=httpStatus;
        this.message=message;
    }
}
