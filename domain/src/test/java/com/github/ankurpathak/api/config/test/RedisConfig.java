package com.github.ankurpathak.api.config.test;

import com.github.ankurpathak.api.annotation.Test;
import com.github.ankurpathak.api.testcontainer.redis.RedisContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@Test
public class RedisConfig {
    @Autowired
    private RedisProperties properties;

    @Bean(initMethod = "start", destroyMethod = "close")
    public RedisContainer redis(){
        RedisContainer redis = new RedisContainer();
        return redis;
    }

    @Bean(destroyMethod = "destroy")
    public JedisConnectionFactory redisConnectionFactory(RedisContainer redis){
        JedisConnectionFactory jedis = new JedisConnectionFactory();
        jedis.setHostName(redis.getContainerIpAddress());
        jedis.setPort(redis.getPort());
        return jedis;
    }

}
