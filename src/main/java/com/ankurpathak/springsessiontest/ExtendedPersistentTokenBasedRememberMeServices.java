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






    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        if (this.alwaysRemember) {
            return true;
        } else {
            if(WebUtil.isAjax(request))
                return Boolean.parseBoolean(request.getHeader(X_REMEMBER_ME_HEADER));
            else
                return super.rememberMeRequested(request, parameter);
        }
    }




    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        if(WebUtil.isAjax(request))
            setHeader(tokens, request, response);
        else
            super.setCookie(tokens, maxAge, request, response);
    }


    public void setHeader(String[] tokens, HttpServletRequest request, HttpServletResponse response){
        String cookieValue = this.encodeCookie(tokens);
        response.setHeader(X_REMEMBER_ME_TOKEN_HEADER, cookieValue);
    }



    @Override
    protected String extractRememberMeCookie(HttpServletRequest request) {
        if(WebUtil.isAjax(request)){
            return request.getHeader(X_REMEMBER_ME_HEADER);
        }else {
            return super.extractRememberMeCookie(request);
        }
    }





}
