package com.nhom7.qlkhachsan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@RedisHash("Product")
public class RedisTest implements Serializable {
    @Id
    private String id;
    private String name;
    private String price;
}
