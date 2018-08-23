package com.ankurpathak.springsessiontest;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExtendedTokenBasedRememberMeServices extends TokenBasedRememberMeServices {
    private boolean alwaysRemember;
    public ExtendedTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    public static final String X_REMEMBER_ME_HEADER = "X-Remember-Me";
    public static final String X_REMEMBER_ME_TOKEN_HEADER = "X-Remember-Me-Token";


    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        if(WebUtil.isAjax(request))
            setHeader(tokens, request, response);
        else
            super.setCookie(tokens, maxAge, request, response);
    }

    public void setHeader(String[] tokens, HttpServletRequest request, HttpServletResponse response){
        String cookieValue = this.encodeCookie(tokens);
        if(!StringUtils.isEmpty(cookieValue))
            response.setHeader(X_REMEMBER_ME_TOKEN_HEADER, cookieValue);
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
    protected String extractRememberMeCookie(HttpServletRequest request) {
        if(WebUtil.isAjax(request)){
            return request.getHeader(X_REMEMBER_ME_HEADER);
        }else {
            return super.extractRememberMeCookie(request);
        }
    }



}
