package com.ankurpathak.springsessiontest;

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

    private static final Logger log = LoggerFactory.getLogger(RuntimeRestExceptionHandler.class);


    private final IFilterService filterService;

    public RestAuthenticationEntryPoint(IFilterService filterService) {
        this.filterService = filterService;
    }

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException ex) throws IOException {
        log.info("message: {} cause: {}", ex.getMessage(), ex.getCause());
        ex.printStackTrace();
        filterService.generateUnauthorized(response);
    }



}