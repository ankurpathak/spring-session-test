package com.ankurpathak.springsessiontest;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final IFilterService filterService;

    public RestAccessDeniedHandler(IFilterService filterService) {
        this.filterService = filterService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {
        filterService.generateForbidden(response);
    }
}
