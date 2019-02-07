package com.github.ankurpathak.app;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

public class ExtendedAnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private String key;

    public ExtendedAnonymousAuthenticationFilter(String key, AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        super(key, CustomUserDetails.ANNONYMOUS_CUSTOM_USER_DETAILS, AuthorityUtils.createAuthorityList(Role.ANONYMOUS_ROLE.privilegesAsArray()));
        this.authenticationDetailsSource = authenticationDetailsSource;
        this.key = key;
    }

    public ExtendedAnonymousAuthenticationFilter(String key) {
        super(key, CustomUserDetails.ANNONYMOUS_CUSTOM_USER_DETAILS, AuthorityUtils.createAuthorityList(Role.ANONYMOUS_ROLE.privilegesAsArray()));
        this.key = key;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(key,
                getPrincipal(), getAuthorities());
        if(authenticationDetailsSource != null)
            auth.setDetails(authenticationDetailsSource.buildDetails(request));
        return auth;
    }



}