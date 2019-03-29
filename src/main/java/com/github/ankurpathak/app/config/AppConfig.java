package com.github.ankurpathak.app.config;

import com.google.gson.Gson;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@Configuration
public class AppConfig {


    private final  RedisOperationsSessionRepository sessionRepository;

    public AppConfig(RedisOperationsSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }




    @Bean
    public TaskExecutor getTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(1);
        threadPoolTaskExecutor.setMaxPoolSize(1);
        return threadPoolTaskExecutor;

    }


    @Bean
    public Client client(){
        return ClientBuilder.newClient();
    }


    @Bean
    public DatabaseReader databaseReader() throws Exception{
        ClassPathResource resource =  new ClassPathResource("GeoLite2-City.mmdb");
        return new DatabaseReader.Builder(resource.getInputStream()).withCache(new CHMCache()).build();
    }


    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }



    @Bean
    public Gson gson(){
        return new Gson();
    }










}





