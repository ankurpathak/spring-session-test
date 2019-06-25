package com.github.ankurpathak.app.security.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RemeberMeTokenResolverDelegateBackServices {
    void setToken(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response);

    String getToken(HttpServletRequest request);


    String encodeToken(String[] tokens);

    boolean getAlwaysRemember();


    boolean getRememberMeRequested(HttpServletRequest request, String parameter);
}