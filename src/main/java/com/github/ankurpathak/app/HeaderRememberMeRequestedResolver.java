package com.github.ankurpathak.app;

import com.github.ankurpathak.app.util.WebUtil;

import javax.servlet.http.HttpServletRequest;

public class HeaderRememberMeRequestedResolver implements IRememberMeRequestedResolver {
    @Override
    public boolean rememberMeRequested(RemeberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request, String parameter) {
        if (rememberMeServices.getAlwaysRemember()) {
            return true;
        } else {
            return WebUtil.isRememberMeRequested(request);
        }
    }


    public static HeaderRememberMeRequestedResolver xRememberMe(){
        return new HeaderRememberMeRequestedResolver();
    }
}
