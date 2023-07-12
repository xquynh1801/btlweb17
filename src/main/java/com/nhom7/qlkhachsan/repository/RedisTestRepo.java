package com.nhom7.qlkhachsan.repository;

import com.nhom7.qlkhachsan.entity.RedisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RedisTestRepo {
    public static final String HASH_KEY = "Product";
    @Autowired
    private RedisTemplate redisTemplate;

    public RedisTest save(RedisTest redisTest){
        redisTemplate.opsForHash().put(HASH_KEY, redisTest.getId(), redisTest);
        return redisTest;
    }

    public List<RedisTest> findAll(){
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public RedisTest findProductById(int id){
        return (RedisTest) redisTemplate.opsForHash().get(HASH_KEY,id);
    }

    public String deleteProduct(int id){
        redisTemplate.opsForHash().delete(HASH_KEY,id);
        return "product removed !!";
    }
}
