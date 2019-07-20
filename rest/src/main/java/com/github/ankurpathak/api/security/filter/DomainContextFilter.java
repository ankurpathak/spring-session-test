package com.github.ankurpathak.api.security.filter;

import com.github.ankurpathak.api.security.dto.DomainContext;
import com.github.ankurpathak.api.security.dto.DomainContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class DomainContextFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        DomainContext context = new DomainContext((HttpServletRequest) servletRequest);
        DomainContextHolder.setContext(context);
        filterChain.doFilter(servletRequest, servletResponse);
        DomainContextHolder.clearContext();
    }
}
