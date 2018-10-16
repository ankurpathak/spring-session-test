package com.ankurpathak.springsessiontest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.HttpSessionIdResolver;



@Configuration
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {

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


}