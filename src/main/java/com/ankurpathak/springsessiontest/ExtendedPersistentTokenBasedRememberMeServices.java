package com.ankurpathak.springsessiontest;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExtendedPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {
    private boolean alwaysRemember;


    public ExtendedPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
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
            return WebUtil.isRememberMeRequested(request);
        }
    }





    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        if (WebUtil.isAjax(request))
            setHeader(tokens, response);
        else
            super.setCookie(tokens, maxAge, request, response);
    }


    public void setHeader(String[] tokens, HttpServletResponse response) {
        String cookieValue = this.encodeCookie(tokens);
        if (!StringUtils.isEmpty(cookieValue))
            WebUtil.setRememberMeToken(response, cookieValue);
    }


    @Override
    protected String extractRememberMeCookie(HttpServletRequest request) {
        if (WebUtil.isAjax(request)) {
            return WebUtil.getRememberMeToken(request);
        } else {
            return super.extractRememberMeCookie(request);
        }
    }


}
