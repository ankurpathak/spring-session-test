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
    protected String extractRememberMeCookie(HttpServletRequest request) {
        if (WebUtil.isAjax(request)) {
            return WebUtil.getRememberMeToken(request);
        } else {
            return super.extractRememberMeCookie(request);
        }
    }


}
