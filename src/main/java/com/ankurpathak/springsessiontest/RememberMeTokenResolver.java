package com.ankurpathak.springsessiontest;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RememberMeTokenResolver implements IRememberMeTokenResolver {

    private final CookieRememberMeTokenResover cookieRememberMeTokenResover;
    private final HeaderRememberMeTokenResolver headerRememberMeTokenResolver;
    private final Type type;

    public RememberMeTokenResolver(Type type, HeaderRememberMeTokenResolver headerRememberMeTokenResolver) {
        this.type = type;
        this.cookieRememberMeTokenResover = new CookieRememberMeTokenResover();
        this.headerRememberMeTokenResolver = headerRememberMeTokenResolver;
    }

    @Override
    public void setToken(RemeberMeTokenResolverDelegateBackServices rememberMeServices, String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        switch (type) {
            case HEADER_ONLY:
                headerRememberMeTokenResolver.setToken(rememberMeServices, tokens, maxAge, request, response);
                break;
            case BOTH_HEADER_FIRST:
                headerRememberMeTokenResolver.setToken(rememberMeServices, tokens, maxAge, request, response);
                cookieRememberMeTokenResover.setToken(rememberMeServices, tokens, maxAge, request, response);
                break;
            case BOTH_COOKIE_FIRST:
                headerRememberMeTokenResolver.setToken(rememberMeServices, tokens, maxAge, request, response);
                cookieRememberMeTokenResover.setToken(rememberMeServices, tokens, maxAge, request, response);
                break;
            case COOKIE_ONLY:
            default:
                cookieRememberMeTokenResover.setToken(rememberMeServices, tokens, maxAge, request, response);
                break;

        }
    }

    @Override
    public String getToken(RemeberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request) {
        String tempToken = null;
        switch (type) {
            case HEADER_ONLY:
                return headerRememberMeTokenResolver.getToken(rememberMeServices, request);
            case BOTH_HEADER_FIRST:
                tempToken = headerRememberMeTokenResolver.getToken(rememberMeServices, request);
                return StringUtils.isNotEmpty(tempToken) ? tempToken : cookieRememberMeTokenResover.getToken(rememberMeServices, request);
            case BOTH_COOKIE_FIRST:
                tempToken = cookieRememberMeTokenResover.getToken(rememberMeServices, request);
                return StringUtils.isNotEmpty(tempToken) ? tempToken : headerRememberMeTokenResolver.getToken(rememberMeServices, request);
            case COOKIE_ONLY:
            default:
                return cookieRememberMeTokenResover.getToken(rememberMeServices, request);
        }
    }


    enum Type {
        COOKIE_ONLY, HEADER_ONLY, BOTH_COOKIE_FIRST, BOTH_HEADER_FIRST
    }
}
