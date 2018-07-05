package com.ankurpathak.springsessiontest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;

public class SecurityContextCompositeImpl extends SecurityContextImpl {
    private final DomainContext domainContext;
    private final SecurityContext securityContext;


    public SecurityContextCompositeImpl(final SecurityContext securityContext, final DomainContext domainContext) {
        this.securityContext = securityContext;
        this.domainContext = domainContext;
    }

    public DomainContext getDomainContext() {
        return domainContext;
    }

    public SecurityContext getSecurityContext() {
        return securityContext;
    }


    @Override
    public Authentication getAuthentication() {
        return securityContext.getAuthentication();
    }

    @Override
    public int hashCode() {
        return securityContext.hashCode();
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        securityContext.setAuthentication(authentication);
    }

    @Override
    public String toString() {
        return securityContext.toString();
    }
}
