package com.github.ankurpathak.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieRememberMeTokenResover implements IRememberMeTokenResolver {


    @Override
    public void setToken(RemeberMeTokenResolverDelegateBackServices rememberMeServices, String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        rememberMeServices.setToken(tokens, maxAge, request, response);
    }

    @Override
    public String getToken(RemeberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request) {
        return rememberMeServices.getToken(request);
    }
}
