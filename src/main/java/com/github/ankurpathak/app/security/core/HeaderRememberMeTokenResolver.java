package com.github.ankurpathak.app.security.core;

import com.github.ankurpathak.app.RemeberMeTokenResolverDelegateBackServices;
import com.github.ankurpathak.app.util.WebUtil;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HeaderRememberMeTokenResolver implements IRememberMeTokenResolver {
    @Override
    public void setToken(RemeberMeTokenResolverDelegateBackServices rememberMeServices, String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        setHeader(rememberMeServices, tokens, response);
    }

    @Override
    public String getToken(RemeberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request) {
        return WebUtil.getRememberMeToken(request);
    }



    public void setHeader(RemeberMeTokenResolverDelegateBackServices rememberMeServices, String[] tokens, HttpServletResponse response) {
        String cookieValue = rememberMeServices.encodeToken(tokens);
        if (!StringUtils.isEmpty(cookieValue))
            WebUtil.setRememberMeToken(response, cookieValue);
    }


    public static HeaderRememberMeTokenResolver xRememberMeToken(){
        return new HeaderRememberMeTokenResolver();
    }

}
