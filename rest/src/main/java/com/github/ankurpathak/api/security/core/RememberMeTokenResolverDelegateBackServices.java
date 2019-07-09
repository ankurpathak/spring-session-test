package com.github.ankurpathak.api.security.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RememberMeTokenResolverDelegateBackServices {
    void setToken(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response);

    String getToken(HttpServletRequest request);


    String encodeToken(String[] tokens);

    boolean getAlwaysRemember();


    boolean getRememberMeRequested(HttpServletRequest request, String parameter);

    void cancelToken(HttpServletRequest request, HttpServletResponse response);
}