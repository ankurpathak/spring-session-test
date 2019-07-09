package com.github.ankurpathak.api.security.core;

import com.github.ankurpathak.api.service.IFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestAccessDeniedHandler.class);
    private final IFilterService filterService;

    public RestAccessDeniedHandler(IFilterService filterService) {
        this.filterService = filterService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {
        logger.error("message: {} cause: {} path: {}", ex.getMessage(), ex.getCause(), request.getRequestURI());
        filterService.generateForbidden(response);
    }
}
