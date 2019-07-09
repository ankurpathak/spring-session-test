package com.github.ankurpathak.api.security.core;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SocialApplicationAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public SocialApplicationAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public SocialApplicationAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
