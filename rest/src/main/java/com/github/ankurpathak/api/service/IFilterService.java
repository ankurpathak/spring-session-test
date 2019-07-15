package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IFilterService {
    void generateUnauthorized(HttpServletResponse response, AuthenticationException ex) throws IOException;

    void generateUnauthorized(HttpServletResponse response) throws IOException;

    void generateSuccess(HttpServletResponse response) throws IOException;

    void generateForbidden(HttpServletResponse response) throws IOException;

    void generateExpiredToken(String token, HttpServletResponse response) throws IOException;

    void generateInvalid(String key, String value, ApiCode code, HttpServletResponse response) throws IOException;

}
