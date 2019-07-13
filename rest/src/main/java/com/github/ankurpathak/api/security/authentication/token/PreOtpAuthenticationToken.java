package com.github.ankurpathak.api.security.authentication.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;

public class PreOtpAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final UsernamePasswordAuthenticationToken authentication;

    public PreOtpAuthenticationToken(UsernamePasswordAuthenticationToken authentication) {
        super(authentication.getPrincipal(), null, Collections.emptySet());
        this.authentication = authentication;
    }

    public UsernamePasswordAuthenticationToken getAuthentication() {
        return authentication;
    }

    @Override
    public Object getDetails() {
       return authentication.getDetails();
    }

    @Override
    public void setDetails(Object details) {
        authentication.setDetails(details);
    }

    @Override
    public boolean isAuthenticated() {
        return authentication.isAuthenticated();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated)
            throws IllegalArgumentException {
        authentication.setAuthenticated(isAuthenticated);
    }

    public static PreOtpAuthenticationToken getInstance(UsernamePasswordAuthenticationToken authentication){
        return new PreOtpAuthenticationToken(authentication);
    }
}