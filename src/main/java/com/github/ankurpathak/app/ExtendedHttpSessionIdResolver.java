package com.github.ankurpathak.app;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.session.web.http.HttpSessionIdResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ExtendedHttpSessionIdResolver implements HttpSessionIdResolver {

    private final Type type;
    private final ExtendedHeaderHttpSessionIdResolver headerHttpSessionIdResolver;
    private final ExtendedCookieHttpSessionIdResolver cookieHttpSessionIdResolver;


    public ExtendedHttpSessionIdResolver(Type type, ExtendedHeaderHttpSessionIdResolver headerHttpSessionIdResolver) {
        this.type = type;
        this.headerHttpSessionIdResolver = headerHttpSessionIdResolver;
        cookieHttpSessionIdResolver = new ExtendedCookieHttpSessionIdResolver();
    }


    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        Set<String> sessionIds = new LinkedHashSet<>();
        switch (type) {
            case HEADER_ONLY:
                sessionIds.addAll(headerHttpSessionIdResolver.resolveSessionIds(request));
                break;
            case BOTH_COOKIE_FIRST:
                sessionIds.addAll(cookieHttpSessionIdResolver.resolveSessionIds(request));
                if(CollectionUtils.isEmpty(sessionIds))
                    sessionIds.addAll(headerHttpSessionIdResolver.resolveSessionIds(request));
                break;
            case BOTH_HEADER_FIRST:
                sessionIds.addAll(headerHttpSessionIdResolver.resolveSessionIds(request));
                if(CollectionUtils.isEmpty(sessionIds))
                    sessionIds.addAll(cookieHttpSessionIdResolver.resolveSessionIds(request));
                break;
            case COOKIE_ONLY:
            default:
                sessionIds.addAll(cookieHttpSessionIdResolver.resolveSessionIds(request));
                break;

        }
        return new ArrayList<>(sessionIds);
    }

    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String name) {
        switch (type) {
            case HEADER_ONLY:
                headerHttpSessionIdResolver.setSessionId(request, response, name);
                break;
            case BOTH_COOKIE_FIRST:
                cookieHttpSessionIdResolver.setSessionId(request, response, name);
                headerHttpSessionIdResolver.setSessionId(request, response, name);
                break;
            case BOTH_HEADER_FIRST:
                headerHttpSessionIdResolver.setSessionId(request, response, name);
                cookieHttpSessionIdResolver.setSessionId(request, response, name);
                break;

            case COOKIE_ONLY:
            default:
                cookieHttpSessionIdResolver.setSessionId(request, response, name);
                break;

        }
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        switch (type) {
            case HEADER_ONLY:
                headerHttpSessionIdResolver.expireSession(request, response);
                break;
            case BOTH_COOKIE_FIRST:
                cookieHttpSessionIdResolver.expireSession(request, response);
                headerHttpSessionIdResolver.expireSession(request, response);
                break;
            case BOTH_HEADER_FIRST:
                headerHttpSessionIdResolver.expireSession(request, response);
                cookieHttpSessionIdResolver.expireSession(request, response);
                break;
            case COOKIE_ONLY:
            default:
                cookieHttpSessionIdResolver.expireSession(request, response);
                break;

        }
    }


    public enum Type {
        HEADER_ONLY, COOKIE_ONLY, BOTH_HEADER_FIRST, BOTH_COOKIE_FIRST
    }


}
