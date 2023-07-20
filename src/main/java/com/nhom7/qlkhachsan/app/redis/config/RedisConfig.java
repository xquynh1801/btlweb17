package com.nhom7.qlkhachsan.app.redis.config;

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
        RedisStandaloneConfiguration jedisConnectionFactory = new RedisStandaloneConfiguration();
        jedisConnectionFactory.setHostName("127.0.0.1");
        jedisConnectionFactory.setPort(6379);
        log.info("Connected to redis,...");
        return new JedisConnectionFactory(jedisConnectionFactory);

    }
    @Bean
    public RedisTemplate<String, Object> template() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }
}
