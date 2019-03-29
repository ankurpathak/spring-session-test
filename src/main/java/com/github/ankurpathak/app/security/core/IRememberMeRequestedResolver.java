package com.github.ankurpathak.app.security.core;

import com.github.ankurpathak.app.RemeberMeTokenResolverDelegateBackServices;

import javax.servlet.http.HttpServletRequest;

public interface IRememberMeRequestedResolver {
    boolean rememberMeRequested(RemeberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request, String parameter);
}
