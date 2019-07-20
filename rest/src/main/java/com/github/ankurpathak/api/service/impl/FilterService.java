package com.github.ankurpathak.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.service.IFilterService;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.service.impl.util.FilterUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class FilterService implements IFilterService {
    private final ObjectMapper objectMapper;
    private final IMessageService messageService;

    public FilterService(ObjectMapper objectMapper, IMessageService messageService) {
        this.objectMapper = objectMapper;
        this.messageService = messageService;
    }

    @Override
    public void generateUnauthorized(HttpServletResponse response, AuthenticationException ex) throws IOException {
        FilterUtil.generateUnauthorized(response, objectMapper, messageService, ex);
    }

    @Override
    public void generateUnauthorized(HttpServletResponse response) throws IOException{
        FilterUtil.generateUnauthorized(response, objectMapper, messageService);
    }

    @Override
    public void generateSuccess(HttpServletResponse response) throws IOException{
        FilterUtil.generateSuccess(response, objectMapper, messageService);
    }

    @Override
    public void generateForbidden(HttpServletResponse response) throws IOException{
        FilterUtil.generateForbidden(response, objectMapper, messageService);
    }

    @Override
    public void generateExpiredToken(String token, HttpServletResponse response) throws IOException {
        FilterUtil.generateExpiredToken(token, response, objectMapper, messageService);
    }

    @Override
    public void generateInvalid(String key, String value, ApiCode code, HttpServletResponse response) throws IOException {
        FilterUtil.generateInvalid(key, value, code, response, objectMapper, messageService);
    }
}
