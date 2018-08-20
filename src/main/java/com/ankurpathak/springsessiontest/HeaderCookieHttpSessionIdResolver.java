package com.ankurpathak.springsessiontest;

import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public class HeaderCookieHttpSessionIdResolver implements HttpSessionIdResolver {

    private final HeaderHttpSessionIdResolver headerHttpSessionIdResolver;
    private final CookieHttpSessionIdResolver cookieHttpSessionIdResolver;

    public HeaderCookieHttpSessionIdResolver(HeaderHttpSessionIdResolver headerHttpSessionIdResolver) {
        this.headerHttpSessionIdResolver = headerHttpSessionIdResolver;
        cookieHttpSessionIdResolver = new CookieHttpSessionIdResolver();
    }


    private HttpSessionIdResolver resolve(HttpServletRequest request){
        HttpSessionIdResolver httpSessionIdResolver = null;
        if(WebUtil.isAjax(request))
            httpSessionIdResolver = headerHttpSessionIdResolver;
        else {
            httpSessionIdResolver = cookieHttpSessionIdResolver;
            request.setAttribute(HeaderCookieHttpSessionIdResolver.class.getName(), cookieHttpSessionIdResolver);
        }
        request.setAttribute(HeaderCookieHttpSessionIdResolver.class.getName(), httpSessionIdResolver);
        return httpSessionIdResolver;
    }

    private HttpSessionIdResolver obtain(HttpServletRequest request){
        HttpSessionIdResolver httpSessionIdResolver = (HttpSessionIdResolver) request.getAttribute(HeaderCookieHttpSessionIdResolver.class.getName());
        if(httpSessionIdResolver == null){
            httpSessionIdResolver = resolve(request);
        }
        return httpSessionIdResolver;
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        return obtain(request).resolveSessionIds(request);
    }

    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String name) {
        obtain(request).setSessionId(request, response, name);
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        obtain(request).expireSession(request, response);
    }
}
