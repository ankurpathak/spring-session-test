package com.github.ankurpathak.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.app.service.impl.util.FilterUtil;
import com.github.ankurpathak.app.service.IFilterService;
import com.github.ankurpathak.app.service.IMessageService;
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
}
