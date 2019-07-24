package com.github.ankurpathak.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.security.core.RestSavedRequestAwareAuthenticationSuccessHandler;
import com.github.ankurpathak.api.security.core.RestSimpleUrlAuthenticationFailureHandler;
import com.github.ankurpathak.api.security.filter.*;
import com.github.ankurpathak.api.service.IBusinessService;
import com.github.ankurpathak.api.service.IFilterService;
import com.github.ankurpathak.api.service.ITokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import java.util.UUID;

import static com.github.ankurpathak.api.constant.ApiPaths.*;

@Configuration
public class FilterConfig {

    private final  PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
    private final RestSimpleUrlAuthenticationFailureHandler restAuthenticationFailureHandler;
    private  final RestSavedRequestAwareAuthenticationSuccessHandler restAuthenticationSuccessHandler;
    private final ObjectMapper objectMapper;
    private final IFilterService filterService;
    private final ITokenService tokenService;
    private final IBusinessService businessService;


    public static final String ANNONYMOUS_KEY = UUID.randomUUID().toString();

    public FilterConfig(PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices, RestSimpleUrlAuthenticationFailureHandler restAuthenticationFailureHandler, RestSavedRequestAwareAuthenticationSuccessHandler restAuthenticationSuccessHandler, ObjectMapper objectMapper, IFilterService filterService, ITokenService tokenService, IBusinessService businessService) {
        this.persistentTokenBasedRememberMeServices = persistentTokenBasedRememberMeServices;
        this.restAuthenticationFailureHandler = restAuthenticationFailureHandler;
        this.restAuthenticationSuccessHandler = restAuthenticationSuccessHandler;
        this.objectMapper = objectMapper;
        this.filterService = filterService;
        this.tokenService = tokenService;
        this.businessService = businessService;
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
        RestUsernamePasswordAuthenticationFilter filter = new RestUsernamePasswordAuthenticationFilter(apiPath(PATH_LOGIN), objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setRememberMeServices(persistentTokenBasedRememberMeServices);
        filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
        return filter;
    }

    protected LoginTokenValidationFilter otpValidationFilter() {
        return new LoginTokenValidationFilter(apiPath(PATH_LOGIN_OTP),filterService, tokenService);
    }


    protected BusinessDetailsFilter businessDetailsFilter(){
        return new BusinessDetailsFilter(businessService);
    }




}
