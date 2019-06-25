package com.github.ankurpathak.app.security.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRememberMeTokenResolver {
    void setToken(RemeberMeTokenResolverDelegateBackServices rememberMeServices, String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response);

    String getToken(RemeberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request);
}
