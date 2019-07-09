package com.github.ankurpathak.api.security.core;

import com.github.ankurpathak.api.util.WebUtil;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HeaderRememberMeTokenResolver implements IRememberMeTokenResolver {
    @Override
    public void setToken(RememberMeTokenResolverDelegateBackServices rememberMeServices, String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        setHeader(rememberMeServices, tokens, response);
    }

    @Override
    public String getToken(RememberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request) {
        return WebUtil.getRememberMeToken(request);
    }

    @Override
    public void cancelToken(RememberMeTokenResolverDelegateBackServices rememberMeServices, HttpServletRequest request, HttpServletResponse response) {
        WebUtil.setRememberMeToken(response, "");
    }


    public void setHeader(RememberMeTokenResolverDelegateBackServices rememberMeServices, String[] tokens, HttpServletResponse response) {
        String cookieValue = rememberMeServices.encodeToken(tokens);
        if (!StringUtils.isEmpty(cookieValue))
            WebUtil.setRememberMeToken(response, cookieValue);
    }


    public static HeaderRememberMeTokenResolver xRememberMeToken(){
        return new HeaderRememberMeTokenResolver();
    }

}
