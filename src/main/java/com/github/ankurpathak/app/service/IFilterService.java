package com.github.ankurpathak.app.service;

import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IFilterService {
    void generateUnauthorized(HttpServletResponse response, AuthenticationException ex) throws IOException;

    void generateUnauthorized(HttpServletResponse response) throws IOException;

    void generateSuccess(HttpServletResponse response) throws IOException;

    void generateForbidden(HttpServletResponse response) throws IOException;

}
