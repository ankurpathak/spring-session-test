package com.github.ankurpathak.api.security.core;

import javax.servlet.http.HttpServletRequest;

public interface IRememberMeRequestedResolver {
    boolean rememberMeRequested(RememberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request, String parameter);
}
