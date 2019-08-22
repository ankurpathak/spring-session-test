package com.github.ankurpathak.api.config.test;

import com.github.ankurpathak.api.annotation.Test;
import com.github.ankurpathak.api.testcontainer.redis.RedisContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

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

    @Bean(destroyMethod = "destroy")
    public RedisConnectionFactory redisConnectionFactory(RedisContainer redis){
        LettuceConnectionFactory jedis = new LettuceConnectionFactory();
        jedis.setHostName(redis.getContainerIpAddress());
        jedis.setPort(redis.getPort());
        return jedis;
    }
}
