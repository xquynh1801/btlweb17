package com.nhom7.qlkhachsan.controller;

import com.nhom7.qlkhachsan.entity.RedisTest;
import com.nhom7.qlkhachsan.repository.RedisTestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class RedisTestController {
    @Autowired
    private RedisTestRepo redisTestRepo;

    @PostMapping
    public RedisTest save(@RequestBody RedisTest product) {
        return redisTestRepo.save(product);
    }

    @GetMapping
    public List<RedisTest> getAllProducts() {
        return redisTestRepo.findAll();
    }

    @GetMapping("/{id}")
    public RedisTest findProduct(@PathVariable int id) {
        return redisTestRepo.findProductById(id);
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable int id) {
        return redisTestRepo.deleteProduct(id);
    }
}
