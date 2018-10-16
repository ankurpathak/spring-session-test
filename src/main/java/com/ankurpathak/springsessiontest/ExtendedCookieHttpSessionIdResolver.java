package com.ankurpathak.springsessiontest;

import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ExtendedCookieHttpSessionIdResolver  implements HttpSessionIdResolver {
    private final CookieHttpSessionIdResolver sessionIdResolver;

    public ExtendedCookieHttpSessionIdResolver() {
        this.sessionIdResolver = new CookieHttpSessionIdResolver();
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        return sessionIdResolver.resolveSessionIds(request);
    }

    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String name) {
        sessionIdResolver.setSessionId(request, response, name);
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        sessionIdResolver.expireSession(request, response);
    }
}
