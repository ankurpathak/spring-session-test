package com.ankurpathak.springsessiontest;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public class ControllerUtil {

    public static void processValidaton(BindingResult result, MessageSource messageSource, HttpServletRequest request){
        if(result.hasErrors()){
            throw new ValidationException(
                    result,
                    MessageUtil.getMessage(messageSource,ApiMessages.VALIDATION),
                    ApiCode.VALIDATION
            );
        }
    }



    public static ResponseEntity<?> processError(MessageSource messageSource, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiResponse.getInstance(
                    ApiCode.UNKNOWN,
                    MessageUtil.getMessage(messageSource,ApiMessages.UNKNOWN)
                )
            );

    }



    public static ResponseEntity<?> processSuccess(MessageSource messageSource, HttpServletRequest request, HttpStatus code){
        return ResponseEntity.status(code)
                .body(
                    ApiResponse.getInstance(
                        ApiCode.SUCCESS,
                        MessageUtil.getMessage(messageSource,ApiMessages.SUCCESS)
                    )
                );
    }
}
