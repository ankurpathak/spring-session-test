package com.ankurpathak.springsessiontest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import static com.ankurpathak.springsessiontest.RequestMappingPaths.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String SUCCESS_URL = "/";


    private final AuthenticationSuccessHandler restAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler restAuthenticationFailureHandler;
    private final AccessDeniedHandler restAccessDeniedHandler;
    private final PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final FilterConfig filterConfig;


    private AuthenticationManager authenticationManager;


    final public AuthenticationManager getAuthenticationManager(){
        ensure(authenticationManager, notNullValue());
        return authenticationManager;
    }


    public WebSecurityConfig(AuthenticationSuccessHandler restAuthenticationSuccessHandler, AuthenticationFailureHandler restAuthenticationFailureHandler, AccessDeniedHandler restAccessDeniedHandler, PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices, RestAuthenticationEntryPoint restAuthenticationEntryPoint, FilterConfig filterConfig) {
        this.restAuthenticationSuccessHandler = restAuthenticationSuccessHandler;
        this.restAuthenticationFailureHandler = restAuthenticationFailureHandler;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
        this.persistentTokenBasedRememberMeServices = persistentTokenBasedRememberMeServices;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.filterConfig = filterConfig;
    }


    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        authenticationManager = super.authenticationManagerBean();
        return authenticationManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()


                .oauth2Login()
                .successHandler(restAuthenticationSuccessHandler)
                .failureHandler(restAuthenticationFailureHandler)
                .permitAll()

                .and()


                .anonymous().authenticationFilter(filterConfig.anonymousAuthenticationFilter())

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
                .antMatchers(HttpMethod.PATCH, apiPath(PATH_CHANGE_PASSWORD)).hasAuthority(Role.Privilege.PRIV_CHANGE_PASSWORD)
                .antMatchers(HttpMethod.PUT, apiPath(PATH_CHANGE_PROFILE)).hasAuthority(Role.Privilege.PRIV_CHANGE_PROFILE)
                .antMatchers(HttpMethod.PATCH, apiPath(PATH_CHANGE_PROFILE)).hasAuthority(Role.Privilege.PRIV_CHANGE_PROFILE)
                .anyRequest()
                .denyAll()

                .and()

                .logout()
                .deleteCookies("JSESSIONID", "SESSION")

                .and()

                .rememberMe()
                .rememberMeServices(persistentTokenBasedRememberMeServices)

                .and()

                .addFilterAt(filterConfig.usernamePasswordAuthenticationFilter(getAuthenticationManager()), UsernamePasswordAuthenticationFilter.class)
                //  .addFilterAfter(socialApplicationAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                //  .addFilterAfter(socialWebAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(filterConfig.securityContextCompositeFilter(), SecurityContextPersistenceFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint).accessDeniedHandler(restAccessDeniedHandler);
    }


}
