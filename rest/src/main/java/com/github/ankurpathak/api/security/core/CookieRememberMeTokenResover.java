package com.github.ankurpathak.api.security.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieRememberMeTokenResover implements IRememberMeTokenResolver {


    @Override
    public void setToken(RememberMeTokenResolverDelegateBackServices rememberMeServices, String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        rememberMeServices.setToken(tokens, maxAge, request, response);
    }

    @Override
    public String getToken(RememberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request) {
        return rememberMeServices.getToken(request);
    }

    @Override
    public void cancelToken(RememberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request, HttpServletResponse response) {
        rememberMeServices.cancelToken(request, response);
    }


}
