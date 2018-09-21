package com.ankurpathak.springsessiontest;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@Configuration
public class AppConfig {


    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider(){
        return new RememberMeAuthenticationProvider(WebSecurityConfig.REMEMBER_ME_KEY);
    }


    @Bean
    public SocialWebAuthenticationProvider socialWebAuthenticationProvider(
            UserDetailsService userDetailsService,
            GoogleService googleService,
            FacebookService facebookService,
            LinkedinService linkedinService){
        return new SocialWebAuthenticationProvider(userDetailsService, googleService, facebookService, linkedinService);
    }


    @Bean
    public SocialApplicationAuthenticationProvider socialApplicationAuthenticationProvider(
            UserDetailsService userDetailsService,
            GoogleService googleService,
            FacebookService facebookService,
            LinkedinService linkedinService){
        return new SocialApplicationAuthenticationProvider(userDetailsService, googleService, facebookService, linkedinService);
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








}





