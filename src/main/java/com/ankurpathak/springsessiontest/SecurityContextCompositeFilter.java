package com.ankurpathak.springsessiontest;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SecurityContextCompositeFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        DomainContext domainContext = new DomainContext((HttpServletRequest) servletRequest);
        ((ExtendedSecurityContextImpl)SecurityContextHolder.getContext()).setDomainContext(domainContext);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
