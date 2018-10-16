package com.ankurpathak.springsessiontest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
public class AuthenticationManagerConfig {

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final RememberMeAuthenticationProvider rememberMeAuthenticationProvider;
    private final SocialWebAuthenticationProvider socialWebAuthenticationProvider;
    private final SocialApplicationAuthenticationProvider socialApplicationAuthenticationProvider;

    public AuthenticationManagerConfig(DaoAuthenticationProvider daoAuthenticationProvider, RememberMeAuthenticationProvider rememberMeAuthenticationProvider, SocialWebAuthenticationProvider socialWebAuthenticationProvider, SocialApplicationAuthenticationProvider socialApplicationAuthenticationProvider) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.rememberMeAuthenticationProvider = rememberMeAuthenticationProvider;
        this.socialWebAuthenticationProvider = socialWebAuthenticationProvider;
        this.socialApplicationAuthenticationProvider = socialApplicationAuthenticationProvider;
    }


    @Autowired
    void globalConfigure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider)
                .authenticationProvider(rememberMeAuthenticationProvider)
                .authenticationProvider(socialWebAuthenticationProvider)
                .authenticationProvider(socialApplicationAuthenticationProvider);
    }

}
