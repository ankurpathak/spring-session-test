package com.github.ankurpathak.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.app.controller.rest.dto.ApiCode;
import com.github.ankurpathak.app.controller.rest.dto.ApiMessages;
import com.github.ankurpathak.app.controller.rest.dto.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class FilterUtil {


    public static void generateForbidden(HttpServletResponse response, ObjectMapper objectMapper, IMessageService messageService) throws IOException {
        generateApiResponse(response, objectMapper, messageService, HttpServletResponse.SC_FORBIDDEN, ApiCode.FORBIDDEN, ApiMessages.FORBIDDEN);
    }

    public static void generateUnauthorized(HttpServletResponse response, ObjectMapper objectMapper, IMessageService messageService, AuthenticationException ex) throws IOException {
        if (ex instanceof SocialProfileNotFoundException) {
            generateApiResponse(response, objectMapper, messageService, HttpServletResponse.SC_UNAUTHORIZED, ApiCode.NOT_FOUND, ApiMessages.NOT_FOUND,
                    Map.of(Params.PROFILE, ((SocialProfileNotFoundException) ex).getProfile()),
                    SocialProfile.class.getSimpleName(), Params.EMAIL, ((SocialProfileNotFoundException) ex).getProfile().getEmail()
            );
        } else if (ex instanceof DisabledException) {
            generateApiResponse(response, objectMapper, messageService, HttpServletResponse.SC_UNAUTHORIZED, ApiCode.ACCOUNT_DISABLED, ApiMessages.ACCOUNT_DISABLED);
        } else if (ex instanceof BadCredentialsException) {
            generateApiResponse(response, objectMapper, messageService, HttpServletResponse.SC_UNAUTHORIZED, ApiCode.BAD_CREDENTIALS, ApiMessages.BAD_CREDENTIALS);
        } else {
            generateApiResponse(response, objectMapper, messageService, HttpServletResponse.SC_UNAUTHORIZED, ApiCode.UNAUTHORIZED, ApiMessages.UNAUTHORIZED);
        }

    }

    public static void generateUnauthorized(HttpServletResponse response, ObjectMapper objectMapper, IMessageService messageService) throws IOException {
        generateUnauthorized(response, objectMapper, messageService, null);
    }


    public static void generateSuccess(HttpServletResponse response, ObjectMapper objectMapper, IMessageService messageService) throws IOException {
        generateApiResponse(response, objectMapper, messageService, HttpServletResponse.SC_OK, ApiCode.SUCCESS, ApiMessages.SUCCESS);
    }


    private static void generateApiResponse(HttpServletResponse response, ObjectMapper objectMapper, IMessageService messageService, int httpStatusCode, ApiCode code, String apiMessageKey) throws IOException {
        generateApiResponse(response, objectMapper, messageService, httpStatusCode, code, apiMessageKey, Collections.emptyMap());
    }

    private static void generateApiResponse(HttpServletResponse response, ObjectMapper objectMapper, IMessageService messageService, int httpStatusCode, ApiCode code, String apiMessageKey, Map<String, Object> extras, String... apiMessageParams) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(httpStatusCode);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(
                    response.getWriter(),
                    ApiResponse.getInstance(
                            code,
                            messageService.getMessage(apiMessageKey, apiMessageParams)
                    ).addExtras(extras)
            );
        }
    }


}
