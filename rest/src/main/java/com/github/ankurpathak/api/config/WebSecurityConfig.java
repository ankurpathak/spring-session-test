package com.github.ankurpathak.api.config;

import com.github.ankurpathak.api.constant.RequestMappingPaths;
import com.github.ankurpathak.api.domain.model.Role;
import com.github.ankurpathak.api.security.core.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

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
    private final SessionRegistry sessionRegistry;
    private final LogoutSuccessHandler restLogoutSuccessHandler;
    //private final OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService;
   // private final OidcUserService customOidcUserService;


    @Autowired
    private AuthenticationManager authenticationManager;





    public WebSecurityConfig(AuthenticationSuccessHandler restAuthenticationSuccessHandler,
                             AuthenticationFailureHandler restAuthenticationFailureHandler,
                             AccessDeniedHandler restAccessDeniedHandler,
                             PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices,
                             RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                             FilterConfig filterConfig,
                             SessionRegistry sessionRegistry,
                             LogoutSuccessHandler restLogoutSuccessHandler
                            // OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService,
                            // OidcUserService customOidcUserService
    ) {
        this.restAuthenticationSuccessHandler = restAuthenticationSuccessHandler;
        this.restAuthenticationFailureHandler = restAuthenticationFailureHandler;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
        this.persistentTokenBasedRememberMeServices = persistentTokenBasedRememberMeServices;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.filterConfig = filterConfig;
        this.sessionRegistry = sessionRegistry;
        this.restLogoutSuccessHandler = restLogoutSuccessHandler;
       // this.customOAuth2UserService = customOAuth2UserService;
      //  this.customOidcUserService = customOidcUserService;
    }


    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .anonymous().authenticationFilter(filterConfig.anonymousAuthenticationFilter())

                .and()

                .authorizeRequests()
                .antMatchers(HttpMethod.GET, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_GET_ME)).hasAuthority(Role.Privilege.PRIV_ADMIN)
                .antMatchers(HttpMethod.POST, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_CREATE_USER)).hasAuthority(Role.Privilege.PRIV_ADMIN)
                .antMatchers(HttpMethod.POST, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_ACCOUNT)).hasAuthority(Role.Privilege.PRIV_ACCOUNT)
                .antMatchers(HttpMethod.GET, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_SEARCH_BY_FIEND_USER)).hasAuthority(Role.Privilege.PRIV_ADMIN)
                .antMatchers(HttpMethod.GET, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_LIST_FIELD_USER)).hasAuthority(Role.Privilege.PRIV_ADMIN)
                .antMatchers(HttpMethod.PUT, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_ACCOUNT_EMAIL)).hasAuthority(Role.Privilege.PRIV_ACCOUNT_EMAIL)
                .antMatchers(HttpMethod.PUT, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_ACCOUNT_ENABLE)).hasAuthority(Role.Privilege.PRIV_ACCOUNT_ENABLE)
                .antMatchers(HttpMethod.PUT, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_FORGET_PASSWORD_EMAIL)).hasAuthority(Role.Privilege.PRIV_FORGET_PASSWORD_EMAIL)
                .antMatchers(HttpMethod.PUT, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_FORGET_PASSWORD_ENABLE)).hasAuthority(Role.Privilege.PRIV_FORGET_PASSWORD_ENABLE)
                .antMatchers(HttpMethod.PUT, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_FORGET_PASSWORD)).hasAuthority(Role.Privilege.PRIV_FORGET_PASSWORD)
                .antMatchers(HttpMethod.GET, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_SEARCH_USER)).hasAuthority(Role.Privilege.PRIV_ADMIN)
                .antMatchers(HttpMethod.PATCH, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_CHANGE_PASSWORD)).hasAuthority(Role.Privilege.PRIV_CHANGE_PASSWORD)
                .antMatchers(HttpMethod.PUT, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_CHANGE_PROFILE)).hasAuthority(Role.Privilege.PRIV_CHANGE_PROFILE)
                .antMatchers(HttpMethod.PATCH, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_CHANGE_PROFILE)).hasAuthority(Role.Privilege.PRIV_CHANGE_PROFILE)
                .mvcMatchers(HttpMethod.GET, RequestMappingPaths.PATH_FAVICON).permitAll()
                .mvcMatchers(HttpMethod.GET, RequestMappingPaths.apiPath(RequestMappingPaths.PATH_REMEMBER_ME)).rememberMe()
                .anyRequest()
                .denyAll()

               // .and()

               // .oauth2Login()
               // .tokenEndpoint()
                //.accessTokenResponseClient(OAuthConfig.authorizationCodeTokenResponseClient())
               // .and()
               // .userInfoEndpoint().userService(customOAuth2UserService).oidcUserService(customOidcUserService)
              //  .and()
               // .successHandler(restAuthenticationSuccessHandler)
             //   .failureHandler(restAuthenticationFailureHandler)
              //  .permitAll()
                .and()
                .logout()
                .logoutUrl(RequestMappingPaths.PATH_LOGOUT)
                .permitAll()
                .deleteCookies("JSESSIONID", "SESSION")
                .logoutSuccessHandler(restLogoutSuccessHandler)
                .and()

                .rememberMe()
                .rememberMeServices(persistentTokenBasedRememberMeServices)

                .and()

                .addFilterAt(filterConfig.usernamePasswordAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                //  .addFilterAfter(socialApplicationAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                //  .addFilterAfter(socialWebAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterConfig.securityContextCompositeFilter(), SecurityContextPersistenceFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint).accessDeniedHandler(restAccessDeniedHandler)
                .and()
                .sessionManagement()
                .maximumSessions(2)
                .maxSessionsPreventsLogin(true)
                .sessionRegistry(sessionRegistry)
        ;
    }



}
