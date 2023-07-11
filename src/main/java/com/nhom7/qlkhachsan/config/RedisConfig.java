package com.nhom7.qlkhachsan.config;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.net.InetAddress;
import java.util.List;

@Configuration
@EnableRedisRepositories
@Slf4j
public class RedisConfig {
    public JedisConnectionFactory jedisConnectionFactory() {
//        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
//        List<RedisNode> redisNodes = List.of(new RedisNode("127.0.0.1", 6379));
//        redisClusterConfiguration.setClusterNodes(redisNodes);
//        redisClusterConfiguration.setMaxRedirects(3);
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(20);
//        jedisPoolConfig.setMinIdle(5);
//        jedisPoolConfig.setMaxTotal(50);
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration, jedisPoolConfig);
//        jedisConnectionFactory.afterPropertiesSet();
//        return jedisConnectionFactory;
        RedisStandaloneConfiguration jedisConnectionFactory = new RedisStandaloneConfiguration();
        jedisConnectionFactory.setHostName("127.0.0.1");
        jedisConnectionFactory.setPort(6379);
        log.info("Connected to redis,...");
        return new JedisConnectionFactory(jedisConnectionFactory);

    }

//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate() {
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(jedisConnectionFactory());
//        // Self defined string Serializer and fastjson Serializer
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        // jackson Serializer
//        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
//        // kv serialize
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setValueSerializer(jsonRedisSerializer);
//        // hash serialize
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

    @Bean
    public RedisTemplate<String, Object> template() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
//        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }
}
