package com.ankurpathak.springsessiontest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class ExtendedPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {
    public static final String X_REMEMBER_ME_HEADER = "X-Remember-Me";
    public static final String X_REMEMBER_ME_TOKEN_HEADER = "X-Remember-Me-Token";
    private boolean alwaysRemember;
    private PersistentTokenRepository tokenRepository;


    public ExtendedPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        this.tokenRepository = tokenRepository;
    }


    @Override
    public void setAlwaysRemember(boolean alwaysRemember) {
        this.alwaysRemember = alwaysRemember;
    }

    /*
    private LoginRequestDto obtainLoginRequest(HttpServletRequest request) {
        return (LoginRequestDto) request.getAttribute(LoginRequestDto.class.getName());

    }


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


    private void addHeader(PersistentRememberMeToken token, HttpServletRequest request, HttpServletResponse response) {
        this.setHeader(new String[]{token.getSeries(), token.getTokenValue()}, request, response);
    }


    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        if (this.alwaysRemember) {
            return true;
        } else {
            return Boolean.parseBoolean(request.getHeader(X_REMEMBER_ME_HEADER));
        }
    }


    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        super.setCookie(tokens, maxAge, request, response);
        setHeader(tokens, request, response);
    }

    public void setHeader(String[] tokens, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = this.encodeCookie(tokens);
        response.setHeader(X_REMEMBER_ME_TOKEN_HEADER, cookieValue);
    }

    @Override
    protected String extractRememberMeCookie(HttpServletRequest request) {
        String token = request.getHeader(X_REMEMBER_ME_TOKEN_HEADER);
        if (StringUtils.isEmpty(token)) {
            token = super.extractRememberMeCookie(request);
        }
        return token;
    }


}
