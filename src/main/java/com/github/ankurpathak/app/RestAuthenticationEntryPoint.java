package com.github.ankurpathak.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public final class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);


    private final IFilterService filterService;

    public RestAuthenticationEntryPoint(IFilterService filterService) {
        this.filterService = filterService;
    }

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException ex) throws IOException {
        logger.error("message: {} cause: {} path: {}", ex.getMessage(), ex.getCause(), request.getRequestURI());
        ex.printStackTrace();
        filterService.generateUnauthorized(response);
    }



}