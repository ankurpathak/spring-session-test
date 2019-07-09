package com.github.ankurpathak.api.config;

import com.github.ankurpathak.api.security.core.ExtendedPersistentTokenBasedRememberMeServices;
import com.github.ankurpathak.api.security.core.IRememberMeRequestedResolver;
import com.github.ankurpathak.api.security.core.IRememberMeTokenResolver;
import com.github.ankurpathak.api.security.core.MongoTokenRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
public class AuthenticationConfig {
    private final  UserDetailsService userDetailsService;

    private final MongoTemplate mongoTemplate;

    private final IRememberMeTokenResolver rememberMeTokenResolver;

    private final IRememberMeRequestedResolver rememberMeRequestedResolver;

    public AuthenticationConfig(UserDetailsService userDetailsService, MongoTemplate mongoTemplate, IRememberMeTokenResolver rememberMeTokenResolver, IRememberMeRequestedResolver rememberMeRequestedResolver) {
        this.userDetailsService = userDetailsService;
        this.mongoTemplate = mongoTemplate;
        this.rememberMeTokenResolver = rememberMeTokenResolver;
        this.rememberMeRequestedResolver = rememberMeRequestedResolver;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
        return new ExtendedPersistentTokenBasedRememberMeServices(AuthenticationProviderConfig.REMEMBER_ME_KEY, userDetailsService, persistentTokenRepository(), rememberMeTokenResolver, rememberMeRequestedResolver);
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new MongoTokenRepositoryImpl(mongoTemplate);
    }

}
