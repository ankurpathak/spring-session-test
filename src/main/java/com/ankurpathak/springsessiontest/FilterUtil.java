package com.ankurpathak.springsessiontest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilterUtil {

    public static void generateForbidden(HttpServletRequest request, HttpServletResponse response, ObjectMapper objectMapper, MessageSource messageSource) throws IOException{
        String message = messageSource.getMessage(ApiMessages.FORBIDDEN, new Object[]{}, "", request.getLocale());
        if(!response.isCommitted()){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), ApiResponse.getInstance(ApiCode.FORBIDDEN, message));
        }
    }

    public static void generateUnauthorized(HttpServletRequest request, HttpServletResponse response, ObjectMapper objectMapper, MessageSource messageSource) throws IOException{
        String message = messageSource.getMessage(ApiMessages.UNAUTHORIZED, new Object[]{}, "", request.getLocale());
        if(!response.isCommitted()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), ApiResponse.getInstance(ApiCode.UNAUTHORIZED, message));
        }
    }


    public static void generateSuccess(HttpServletRequest request, HttpServletResponse response, ObjectMapper objectMapper, MessageSource messageSource) throws IOException{
        String message = messageSource.getMessage(ApiMessages.SUCCESS, new Object[]{}, "", request.getLocale());
        if(!response.isCommitted()){
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), ApiResponse.getInstance(ApiCode.SUCCESS, message));
        }
    }







}
