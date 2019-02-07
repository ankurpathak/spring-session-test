package com.github.ankurpathak.app;

import javax.servlet.http.HttpServletRequest;

public interface IRememberMeRequestedResolver {
    boolean rememberMeRequested(RemeberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request, String parameter);
}
