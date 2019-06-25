package com.github.ankurpathak.app.security.core;

import javax.servlet.http.HttpServletRequest;

public interface IRememberMeRequestedResolver {
    boolean rememberMeRequested(RemeberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request, String parameter);
}
