package com.github.ankurpathak.api.security.core;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExtendedTokenBasedRememberMeServices extends TokenBasedRememberMeServices implements RememberMeTokenResolverDelegateBackServices {
    private boolean alwaysRemember;
    private final IRememberMeTokenResolver rememberMeTokenResolver;
    private final IRememberMeRequestedResolver rememberMeRequestedResolver;

    public ExtendedTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, IRememberMeTokenResolver rememberMeTokenResolver, IRememberMeRequestedResolver rememberMeRequestedResolver) {
        super(key, userDetailsService);
        this.rememberMeTokenResolver = rememberMeTokenResolver;
        this.rememberMeRequestedResolver = rememberMeRequestedResolver;
    }


    //Token Resolver
    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        rememberMeTokenResolver.setToken(this, tokens, maxAge, request, response);
    }


    @Override
    protected String extractRememberMeCookie(HttpServletRequest request) {
        return rememberMeTokenResolver.getToken(this, request);
    }

    @Override
    protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
        rememberMeTokenResolver.cancelToken(this, request, response);
    }
    //Token Resolver




    //Requested Me
    @Override
    public void setAlwaysRemember(boolean alwaysRemember) {
        super.setAlwaysRemember(alwaysRemember);
        this.alwaysRemember = alwaysRemember;
    }


    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        return rememberMeRequestedResolver.rememberMeRequested(this, request, parameter);
    }
    //Requested Me




    //Delegate Back
    @Override
    public void setToken(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        super.setCookie(tokens, maxAge, request, response);
    }

    @Override
    public String getToken(HttpServletRequest request) {
        return super.extractRememberMeCookie(request);
    }

    @Override
    public String encodeToken(String[] tokens) {
        return super.encodeCookie(tokens);
    }

    @Override
    public boolean getAlwaysRemember() {
        return this.alwaysRemember;
    }

    @Override
    public boolean getRememberMeRequested(HttpServletRequest request, String parameter) {
        return super.rememberMeRequested(request, parameter);
    }

    @Override
    public void cancelToken(HttpServletRequest request, HttpServletResponse response) {
        super.cancelCookie(request, response);
    }
    //Delegate Back
}
