package com.ankurpathak.springsessiontest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilterUtil {


    public static void generateForbidden(HttpServletResponse response, ObjectMapper objectMapper, IMessageService messageService) throws IOException {
        generateUnauthorized(response, objectMapper, messageService, null);
    }

    public static void generateUnauthorized(HttpServletResponse response, ObjectMapper objectMapper, IMessageService messageService, AuthenticationException ex) throws IOException {
        if (!response.isCommitted()) {
            if (ex instanceof SocialProfileNotFoundException) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(
                        response.getWriter(),
                        ApiResponse.getInstance(
                                ApiCode.UNAUTHORIZED,
                                messageService.getMessage(ApiMessages.NOT_FOUND, SocialProfile.class.getSimpleName(), "email", ((SocialProfileNotFoundException) ex).getProfile().getEmail())
                        ).addExtra("profile", ((SocialProfileNotFoundException) ex).getProfile())
                );
            } else if (ex instanceof DisabledException) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(
                        response.getWriter(),
                        ApiResponse.getInstance(
                                ApiCode.ACCOUNT_DISABLED,
                                messageService.getMessage(ApiMessages.ACCOUNT_DISABLED)
                        )
                );
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(
                        response.getWriter(),
                        ApiResponse.getInstance(
                                ApiCode.UNAUTHORIZED,
                                messageService.getMessage(ApiMessages.UNAUTHORIZED)
                        )
                );
            }
        }
    }

    public static void generateUnauthorized(HttpServletResponse response, ObjectMapper objectMapper, IMessageService messageService) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(
                    response.getWriter(),
                    ApiResponse.getInstance(
                            ApiCode.UNAUTHORIZED,
                            messageService.getMessage(ApiMessages.UNAUTHORIZED)
                    )
            );
        }
    }


    public static void generateSuccess(HttpServletResponse response, ObjectMapper objectMapper, IMessageService messageService) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(
                    response.getWriter(),
                    ApiResponse.getInstance(
                            ApiCode.SUCCESS,
                            messageService.getMessage(ApiMessages.SUCCESS)
                    )
            );
        }
    }


}
