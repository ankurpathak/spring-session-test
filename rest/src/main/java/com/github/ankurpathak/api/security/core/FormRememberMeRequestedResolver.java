package com.github.ankurpathak.api.security.core;

import javax.servlet.http.HttpServletRequest;

public class FormRememberMeRequestedResolver implements IRememberMeRequestedResolver {
    @Override
    public boolean rememberMeRequested(RemeberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request, String parameter) {
        return rememberMeServices.getRememberMeRequested(request, parameter);
    }
}
