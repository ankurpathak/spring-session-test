package com.ankurpathak.springsessiontest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class ExtendedTokenBasedRememberMeServices extends TokenBasedRememberMeServices {
    private boolean alwaysRemember;
    public ExtendedTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    public static final String X_REMEMBER_ME_HEADER = "X-Remember-Me";
    public static final String X_REMEMBER_ME_TOKEN_HEADER = "X-Remember-Me-Token";


    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        super.setCookie(tokens, maxAge, request, response);
        setHeader(tokens, request, response);
    }

    public void setHeader(String[] tokens, HttpServletRequest request, HttpServletResponse response){
        String cookieValue = this.encodeCookie(tokens);
        response.setHeader(X_REMEMBER_ME_TOKEN_HEADER, cookieValue);
    }

    @Override
    public void setAlwaysRemember(boolean alwaysRemember) {
        this.alwaysRemember = alwaysRemember;
    }

    private LoginRequestDto obtainLoginRequest(HttpServletRequest request) {
        return (LoginRequestDto) request.getAttribute(LoginRequestDto.class.getName());
    }


    /*
    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        if (this.alwaysRemember) {
            return true;
        } else {
            LoginRequestDto dto = obtainLoginRequest(request);
            return dto != null && dto.isRememberMe();
        }
    }

    */


    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        if (this.alwaysRemember) {
            return true;
        } else {
            return Boolean.parseBoolean(request.getHeader(X_REMEMBER_ME_HEADER));
        }
    }


    protected String extractRememberMeCookie(HttpServletRequest request) {
        String token = request.getHeader(X_REMEMBER_ME_HEADER);
        if (StringUtils.isEmpty(token)) {
            token = super.extractRememberMeCookie(request);
        }
        return token;
    }



}
