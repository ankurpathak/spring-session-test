package com.ankurpathak.springsessiontest;

import javax.servlet.http.HttpServletRequest;

public interface IRememberMeRequestedResolver {
    boolean rememberMeRequested(RemeberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request, String parameter);
}
