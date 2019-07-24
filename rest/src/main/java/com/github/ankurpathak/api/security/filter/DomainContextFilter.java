package com.github.ankurpathak.api.security.filter;

import com.github.ankurpathak.api.security.dto.DomainContext;
import com.github.ankurpathak.api.security.dto.DomainContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DomainContextFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            DomainContext context = new DomainContext(request);
            DomainContextHolder.setContext(context);
            filterChain.doFilter(request, response);
        }finally {
            DomainContextHolder.clearContext();
        }
    }


}
