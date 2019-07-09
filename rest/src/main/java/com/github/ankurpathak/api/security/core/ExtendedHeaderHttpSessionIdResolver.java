package com.github.ankurpathak.api.security.core;

import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ExtendedHeaderHttpSessionIdResolver implements HttpSessionIdResolver {
    private final HeaderHttpSessionIdResolver sessionIdResolver;

    public ExtendedHeaderHttpSessionIdResolver(HeaderHttpSessionIdResolver sessionIdResolver) {
        this.sessionIdResolver = sessionIdResolver;
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


    public static ExtendedHeaderHttpSessionIdResolver xAuthToken() {
        return new ExtendedHeaderHttpSessionIdResolver(HeaderHttpSessionIdResolver.xAuthToken());
    }

    public static ExtendedHeaderHttpSessionIdResolver authenticationInfo() {
        return new ExtendedHeaderHttpSessionIdResolver(HeaderHttpSessionIdResolver.authenticationInfo());
    }
}
