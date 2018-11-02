package com.ankurpathak.springsessiontest;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class RestLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {


    private final IFilterService filterService;

    public RestLogoutSuccessHandler(IFilterService filterService) {
        this.filterService = filterService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        filterService.generateSuccess(response);
    }
}
