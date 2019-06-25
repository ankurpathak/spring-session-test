package com.github.ankurpathak.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.app.security.core.RestSavedRequestAwareAuthenticationSuccessHandler;
import com.github.ankurpathak.app.security.core.RestSimpleUrlAuthenticationFailureHandler;
import com.github.ankurpathak.app.security.filter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import java.util.UUID;

@Configuration
public class FilterConfig {

    @Autowired
    private final  PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
    @Autowired
    private final RestSimpleUrlAuthenticationFailureHandler restAuthenticationFailureHandler;
    @Autowired
    private  final RestSavedRequestAwareAuthenticationSuccessHandler restAuthenticationSuccessHandler;
    @Autowired
    private final ObjectMapper objectMapper;





    public static final String ANNONYMOUS_KEY = UUID.randomUUID().toString();

    public FilterConfig(PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices, RestSimpleUrlAuthenticationFailureHandler restAuthenticationFailureHandler, RestSavedRequestAwareAuthenticationSuccessHandler restAuthenticationSuccessHandler, ObjectMapper objectMapper) {
        this.persistentTokenBasedRememberMeServices = persistentTokenBasedRememberMeServices;
        this.restAuthenticationFailureHandler = restAuthenticationFailureHandler;
        this.restAuthenticationSuccessHandler = restAuthenticationSuccessHandler;
        this.objectMapper = objectMapper;
    }


    protected AnonymousAuthenticationFilter anonymousAuthenticationFilter() {
        return new ExtendedAnonymousAuthenticationFilter(ANNONYMOUS_KEY);
    }



    protected SocialWebAuthenticationFilter socialWebAuthenticationFilter(AuthenticationManager authenticationManager) {
        SocialWebAuthenticationFilter filter = new SocialWebAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setRememberMeServices(persistentTokenBasedRememberMeServices);
        filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
        return filter;
    }

    protected SocialApplicationAuthenticationFilter socialApplicationAuthenticationFilter(AuthenticationManager authenticationManager) {
        SocialApplicationAuthenticationFilter filter = new SocialApplicationAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setRememberMeServices(persistentTokenBasedRememberMeServices);
        filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
        return filter;
    }

    protected DomainContextFilter securityContextCompositeFilter() {
        return new DomainContextFilter();
    }


    protected RestUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        RestUsernamePasswordAuthenticationFilter filter = new RestUsernamePasswordAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setRememberMeServices(persistentTokenBasedRememberMeServices);
        filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
        return filter;
    }




}
