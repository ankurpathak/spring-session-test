package com.ankurpathak.springsessiontest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class AuthenticationProviderConfig {

    public static final String REMEMBER_ME_KEY = UUID.randomUUID().toString();


    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final IGoogleService googleService;
    private final ILinkedinService linkedinService;
    private final IFacebookService facebookService;

    public AuthenticationProviderConfig(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, IGoogleService googleService, ILinkedinService linkedinService, IFacebookService facebookService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.googleService = googleService;
        this.linkedinService = linkedinService;
        this.facebookService = facebookService;
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider(){
        return new RememberMeAuthenticationProvider(REMEMBER_ME_KEY);
    }


    @Bean
    public SocialWebAuthenticationProvider socialWebAuthenticationProvider(){
             return new SocialWebAuthenticationProvider(userDetailsService, googleService, facebookService, linkedinService);
    }


    @Bean
    public SocialApplicationAuthenticationProvider socialApplicationAuthenticationProvider() {
        return new SocialApplicationAuthenticationProvider(userDetailsService, googleService, facebookService, linkedinService);
    }

}
