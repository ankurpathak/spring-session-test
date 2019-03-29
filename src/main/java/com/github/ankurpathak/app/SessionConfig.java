package com.github.ankurpathak.app;

import com.github.ankurpathak.app.security.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.web.http.HttpSessionIdResolver;



@Configuration
public class SessionConfig {


    @Bean
    public HttpSessionIdResolver httpSessionStrategy() {
        return new ExtendedHttpSessionIdResolver(ExtendedHttpSessionIdResolver.Type.BOTH_HEADER_FIRST, ExtendedHeaderHttpSessionIdResolver.xAuthToken());
    }


    @Bean
    public IRememberMeTokenResolver rememberMeTokenResolver(){
        return new RememberMeTokenResolver(RememberMeTokenResolver.Type.BOTH_HEADER_FIRST, HeaderRememberMeTokenResolver.xRememberMeToken());
    }


    @Bean
    public IRememberMeRequestedResolver rememberMeRequestedResolver(){
        return HeaderRememberMeRequestedResolver.xRememberMe();
    }


    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


    //config set notify-keyspace-events KEA
    //config get notify-keyspace-events



}