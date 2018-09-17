package com.ankurpathak.springsessiontest;

import com.ankurpathak.springsessiontest.controller.InvalidTokenException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

public class ControllerUtil {

    private static void processValidation(BindingResult result, MessageSource messageSource, HttpServletRequest request, ApiCode code, String message) {
        if (result.hasErrors()) {
            throw new ValidationException(
                    result,
                    MessageUtil.getMessage(messageSource, message),
                    code
            );
        }
    }


    public static void processValidation(BindingResult result, MessageSource messageSource, HttpServletRequest request) {
        processValidation(result, messageSource, request, ApiCode.VALIDATION, ApiMessages.VALIDATION);
    }


    public static void processValidationForFound(BindingResult result, MessageSource messageSource, HttpServletRequest request, FoundException ex) {
        if (result.hasErrors()) {
            throw new ValidationException(
                    result,
                    MessageUtil.getMessage(messageSource,ApiMessages.FOUND, ex.getEntity(), ex.getProperty(), ex.getId()),
                    ex.getCode()
            );
        }
    }

    public static ResponseEntity<?> processError(MessageSource messageSource, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ApiResponse.getInstance(
                                ApiCode.UNKNOWN,
                                MessageUtil.getMessage(messageSource, ApiMessages.UNKNOWN)
                        )
                );

    }


    public static ResponseEntity<?> processSuccess(MessageSource messageSource, HttpServletRequest request, HttpStatus code, Map<String, Object> extras) {
        return ResponseEntity.status(code)
                .body(
                        ApiResponse.getInstance(
                                ApiCode.SUCCESS,
                                MessageUtil.getMessage(messageSource, ApiMessages.SUCCESS)
                        ).addExtras(extras)
                );
    }


    public static ResponseEntity<?> processSuccessNoContent(){
        return ResponseEntity.noContent().build();
    }


    private static ResponseEntity<?> processSuccess(MessageSource messageSource, HttpServletRequest request, HttpStatus code) {
        return processSuccess(messageSource, request, code, Collections.emptyMap());
    }

    public static ResponseEntity<?> processSuccess(MessageSource messageSource, HttpServletRequest request) {
        return processSuccess(messageSource, request, HttpStatus.OK, Collections.emptyMap());
    }

    public static ResponseEntity<?> processSuccess(MessageSource messageSource, HttpServletRequest request, Map<String, Object> extras) {
        return processSuccess(messageSource, request, HttpStatus.OK, extras);
    }

    public static ResponseEntity<?> processSuccessCreated(MessageSource messageSource, HttpServletRequest request) {
        return processSuccess(messageSource, request, HttpStatus.CREATED, Collections.emptyMap());
    }

    public static ResponseEntity<?> processSuccessCreated(MessageSource messageSource, HttpServletRequest request, Map<String, Object> extras) {
        return processSuccess(messageSource, request, HttpStatus.CREATED, extras);
    }


    public static ResponseEntity<?> processTokenStatus(Token.TokenStatus status, String token, MessageSource messageSource, HttpServletRequest request){
        switch (status) {
            case VALID:
                return ControllerUtil.processSuccess(messageSource, request);

            case EXPIRED:
                throw new InvalidTokenException(
                        MessageUtil.getMessage(messageSource, ApiMessages.EXPIRED_TOKEN, token),
                        ApiCode.EXPIRED_TOKEN
                );
            case INVALID:
            default:
                throw new InvalidTokenException(
                        MessageUtil.getMessage(messageSource, ApiMessages.INVALID_TOKEN, token),
                        ApiCode.INVALID_TOKEN
                );
        }
    }
}
