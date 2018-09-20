package com.ankurpathak.springsessiontest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import static com.ankurpathak.springsessiontest.RequestMappingPaths.*;


@Configuration
//@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String SUCCESS_URL = "/";


    @Autowired
    private MongoTemplate mongoTemplate;
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
    private RememberMeServices persistentTokenBasedRememberMeServices;
    @Autowired
    private SocialWebAuthenticationProvider socialWebAuthenticationProvider;
    @Autowired
    private SocialApplicationAuthenticationProvider socialApplicationAuthenticationProvider;


    @Bean
    @Lazy
    protected RestUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() {
        RestUsernamePasswordAuthenticationFilter filter = new RestUsernamePasswordAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setRememberMeServices(persistentTokenBasedRememberMeServices);
        filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
        return filter;
    }


    public static final String REMEMBER_ME_KEY = "3deb2240-b5d0-49b9-801c-a88d541e7ed1";
    public static final String ANNONYMOUS_KEY = "18f618ee-fa0f-4147-920c-8659f672f4d2";


    @Autowired
    void globalConfigure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider)
                .authenticationProvider(rememberMeAuthenticationProvider)
                .authenticationProvider(socialWebAuthenticationProvider)
                .authenticationProvider(socialApplicationAuthenticationProvider);
    }


    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .anonymous().authenticationFilter(anonymousAuthenticationFilter())

                .and()

                .authorizeRequests()
                .antMatchers(HttpMethod.GET, apiPath(PATH_GET_ME)).hasAuthority(Role.Privilege.PRIV_ADMIN)
                .antMatchers(HttpMethod.POST, apiPath(PATH_CREATE_USER)).hasAuthority(Role.Privilege.PRIV_ADMIN)
                .antMatchers(HttpMethod.POST, apiPath(PATH_ACCOUNT)).hasAuthority(Role.Privilege.PRIV_ACCOUNT)
                .antMatchers(HttpMethod.GET, apiPath(PATH_SEARCH_BY_FIEND_USER)).hasAuthority(Role.Privilege.PRIV_ADMIN)
                .antMatchers(HttpMethod.GET, apiPath(PATH_LIST_FIELD_USER)).hasAuthority(Role.Privilege.PRIV_ADMIN)
                .antMatchers(HttpMethod.PUT, apiPath(PATH_ACCOUNT_EMAIL)).hasAuthority(Role.Privilege.PRIV_ACCOUNT_EMAIL)
                .antMatchers(HttpMethod.PUT, apiPath(PATH_ACCOUNT_ENABLE)).hasAuthority(Role.Privilege.PRIV_ACCOUNT_ENABLE)
                .antMatchers(HttpMethod.PUT, apiPath(PATH_FORGET_PASSWORD_EMAIL)).hasAuthority(Role.Privilege.PRIV_FORGET_PASSWORD_EMAIL)
                .antMatchers(HttpMethod.PUT, apiPath(PATH_FORGET_PASSWORD_ENABLE)).hasAuthority(Role.Privilege.PRIV_FORGET_PASSWORD_ENABLE)
                .antMatchers(HttpMethod.PUT, apiPath(PATH_FORGET_PASSWORD)).hasAuthority(Role.Privilege.PRIV_FORGET_PASSWORD)
                .antMatchers(HttpMethod.GET, apiPath(PATH_SEARCH_USER)).hasAuthority(Role.Privilege.PRIV_ADMIN)
                .anyRequest()
                .denyAll()

                .and()

                .logout()
                .deleteCookies("JSESSIONID", "SESSION")

                .and()

                .rememberMe()
                .rememberMeServices(persistentTokenBasedRememberMeServices())

                .and()

                .addFilterAt(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(socialApplicationAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(socialWebAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(securityContextCompositeFilter(), SecurityContextPersistenceFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint).accessDeniedHandler(accessDeniedHandler);
    }


    @Bean
    @Lazy
    public SocialWebAuthenticationFilter socialWebAuthenticationFilter() {
        SocialWebAuthenticationFilter filter = new SocialWebAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setRememberMeServices(persistentTokenBasedRememberMeServices);
        filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
        return filter;
    }

    @Bean
    @Lazy
    public SocialApplicationAuthenticationFilter socialApplicationAuthenticationFilter() {
        SocialApplicationAuthenticationFilter filter = new SocialApplicationAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setRememberMeServices(persistentTokenBasedRememberMeServices);
        filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
        return filter;
    }

    @Autowired
    private AuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private AuthenticationSuccessHandler restAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler restAuthenticationFailureHandler;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;


    @Bean
    public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
        return new ExtendedPersistentTokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService, persistentTokenRepository());
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        MongoTokenRepositoryImpl tokenRepository = new MongoTokenRepositoryImpl(mongoTemplate);
        return tokenRepository;
    }

    @Bean
    @Lazy
    public SecurityContextCompositeFilter securityContextCompositeFilter() {
        return new SecurityContextCompositeFilter();
    }

    @Bean
    @Lazy
    public AnonymousAuthenticationFilter anonymousAuthenticationFilter() {
        return new ExtendedAnonymousAuthenticationFilter(ANNONYMOUS_KEY);
    }


}
