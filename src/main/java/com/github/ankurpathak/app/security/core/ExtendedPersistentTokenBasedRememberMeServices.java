package com.github.ankurpathak.app.security.core;

import com.github.ankurpathak.app.RemeberMeTokenResolverDelegateBackServices;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExtendedPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices implements RemeberMeTokenResolverDelegateBackServices {
    private boolean alwaysRemember;


    private final IRememberMeTokenResolver rememberMeTokenResolver;
    private final IRememberMeRequestedResolver rememberMeRequestedResolver;


    public ExtendedPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository, IRememberMeTokenResolver rememberMeTokenResolver, IRememberMeRequestedResolver rememberMeRequestedResolver) {
        super(key, userDetailsService, tokenRepository);
        this.rememberMeTokenResolver = rememberMeTokenResolver;
        this.rememberMeRequestedResolver = rememberMeRequestedResolver;
    }




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







    //Token Resolver
    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        rememberMeTokenResolver.setToken(this, tokens, maxAge, request, response);
    }


    @Override
    protected String extractRememberMeCookie(HttpServletRequest request) {
        return rememberMeTokenResolver.getToken(this, request);
    }
    //Token Resolver








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
        return alwaysRemember;
    }

    @Override
    public boolean getRememberMeRequested(HttpServletRequest request, String parameter) {
        return super.rememberMeRequested(request, parameter);
    }
    //Delegate Back


}
