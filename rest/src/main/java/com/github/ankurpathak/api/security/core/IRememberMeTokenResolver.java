package com.github.ankurpathak.api.security.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRememberMeTokenResolver {
    void setToken(RememberMeTokenResolverDelegateBackServices rememberMeServices, String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response);

    String getToken(RememberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request);

    void  cancelToken(RememberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request, HttpServletResponse response);
}
