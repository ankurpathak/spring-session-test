package com.ankurpathak.springsessiontest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;


@Configuration
//@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String SUCCESS_URL = "/";


    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    protected RestUsernamePasswordAuthenticationFilter buildUsernamePasswordAuthenticationFilter(
            ObjectMapper objectMapper,
            AuthenticationManager authenticationManager,
            PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices,
            AuthenticationSuccessHandler restAuthenticationSuccessHandler,
            AuthenticationFailureHandler restAuthenticationFailureHandler
    ) {
        RestUsernamePasswordAuthenticationFilter filter = new RestUsernamePasswordAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setRememberMeServices(persistentTokenBasedRememberMeServices);
        filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
        return filter;
    }


    public static final String REMEMBER_ME_KEY = "3deb2240-b5d0-49b9-801c-a88d541e7ed1";

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;


    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    void globalConfigure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider)
                .authenticationProvider(rememberMeAuthenticationProvider);
    }


    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .hasAuthority(Role.ROLE_ADMIN)
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .and()
                .rememberMe()
                .rememberMeServices(persistentTokenBasedRememberMeServices())
                .and()
                .addFilterAt(
                        buildUsernamePasswordAuthenticationFilter(
                                objectMapper,
                                authenticationManager,
                                persistentTokenBasedRememberMeServices(),
                                restAuthenticationSuccessHandler,
                                restAuthenticationFailureHandler
                        ), UsernamePasswordAuthenticationFilter.class
                )
                .addFilterAfter(buildSecurityContextCompositeFilter(), SecurityContextPersistenceFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }


    @Autowired
    private AuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private AuthenticationSuccessHandler restAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler restAuthenticationFailureHandler;


    @Bean
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        return new ExtendedTokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService);
    }


    @Bean
    public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
        return new ExtendedPersistentTokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService, persistentTokenRepository());
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        MongoTokenRepositoryImpl tokenRepository = new MongoTokenRepositoryImpl(mongoTemplate);
        return tokenRepository;
    }


    protected SecurityContextCompositeFilter buildSecurityContextCompositeFilter(){
        return new SecurityContextCompositeFilter();
    }

}
