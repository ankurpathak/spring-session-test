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
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import static com.ankurpathak.springsessiontest.RequestMappingPaths.*;


@Configuration
//@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String SUCCESS_URL = "/";


    @Autowired private MongoTemplate mongoTemplate;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private DaoAuthenticationProvider daoAuthenticationProvider;
    @Autowired private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private RememberMeServices persistentTokenBasedRememberMeServices;








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
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, apiPath(PATH_GET_ME)).hasAuthority(Role.ROLE_ADMIN)
                .antMatchers(HttpMethod.GET, apiPath(PATH_GET_USER)).hasAuthority(Role.ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, apiPath(PATH_CREATE_USER)).hasAuthority(Role.ROLE_ADMIN)
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
                .addFilterAfter(securityContextCompositeFilter(), SecurityContextPersistenceFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint).accessDeniedHandler(accessDeniedHandler);
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
    public SecurityContextCompositeFilter securityContextCompositeFilter(){
        return new SecurityContextCompositeFilter();
    }

}
