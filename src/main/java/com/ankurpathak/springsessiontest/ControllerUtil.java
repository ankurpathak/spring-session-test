package com.ankurpathak.springsessiontest;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public class ControllerUtil {

    public static void processValidaton(BindingResult result, MessageSource messageSource, HttpServletRequest request){
        if(result.hasErrors()){
            String message = messageSource.getMessage(ApiMessages.VALIDATION, null,"", request.getLocale());
            throw new ValidationException(result, message, ApiCode.VALIDATION);
        }
    }


    public static ResponseEntity<?> processSuccess(MessageSource messageSource, HttpServletRequest request, HttpStatus code){
        String message = messageSource.getMessage(ApiMessages.SUCCESS, null, "", request.getLocale());
        return ResponseEntity.status(code).body(ApiResponse.getInstance(ApiCode.SUCCESS, message));
    }
}
