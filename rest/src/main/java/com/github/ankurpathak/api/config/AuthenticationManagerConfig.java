package com.github.ankurpathak.api.config;

import com.github.ankurpathak.api.security.authentication.provider.LoginTokenGeneratorAuthenticationProvider;
import com.github.ankurpathak.api.security.authentication.provider.SocialApplicationAuthenticationProvider;
import com.github.ankurpathak.api.security.authentication.provider.SocialWebAuthenticationProvider;
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
    private final LoginTokenGeneratorAuthenticationProvider loginTokenGeneratorAuthenticationProvider;

    public AuthenticationManagerConfig(DaoAuthenticationProvider daoAuthenticationProvider, RememberMeAuthenticationProvider rememberMeAuthenticationProvider, SocialWebAuthenticationProvider socialWebAuthenticationProvider, SocialApplicationAuthenticationProvider socialApplicationAuthenticationProvider, LoginTokenGeneratorAuthenticationProvider loginTokenGeneratorAuthenticationProvider) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.rememberMeAuthenticationProvider = rememberMeAuthenticationProvider;
        this.socialWebAuthenticationProvider = socialWebAuthenticationProvider;
        this.socialApplicationAuthenticationProvider = socialApplicationAuthenticationProvider;
        this.loginTokenGeneratorAuthenticationProvider = loginTokenGeneratorAuthenticationProvider;
    }


    @Autowired
    void globalConfigure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginTokenGeneratorAuthenticationProvider)
                .authenticationProvider(rememberMeAuthenticationProvider)
                .authenticationProvider(socialWebAuthenticationProvider)
                .authenticationProvider(socialApplicationAuthenticationProvider);
    }

}
