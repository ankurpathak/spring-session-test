package com.github.ankurpathak.api.config;

import com.github.ankurpathak.api.annotation.Test;
import com.github.ankurpathak.api.testcontainer.redis.RedisContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@Test
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {
    @Autowired
    private RedisProperties properties;

    @Bean(initMethod = "start", destroyMethod = "close")
    public RedisContainer redis(){
        RedisContainer redis = new RedisContainer();
        return redis;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        RedisContainer redis = redis();
        JedisConnectionFactory jedis = new JedisConnectionFactory();
        jedis.setHostName(redis.getContainerIpAddress());
        jedis.setPort(redis.getPort());
        return jedis;
    }
}
