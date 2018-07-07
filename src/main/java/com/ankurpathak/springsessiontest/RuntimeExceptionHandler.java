package com.ankurpathak.springsessiontest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RuntimeExceptionHandler extends ResponseEntityExceptionHandler {


    @Autowired
    private MessageSource messageSource;


    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        String message = messageSource.getMessage(ApiMessages.NOT_FOUND, new Object[]{ex.getEntity(),ex.getProperty(), ex.getId()}, "", request.getLocale());
        return handleExceptionInternal(ex, ApiResponse.getInstance(ex.getCode(), message), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler({FoundException.class})
    public ResponseEntity<Object> handleFoundException(FoundException ex, WebRequest request) {
        String message = messageSource.getMessage(ApiMessages.FOUND, new Object[]{ex.getEntity(), ex.getProperty(), ex.getId()}, "", request.getLocale());
        return handleExceptionInternal(ex, ApiResponse.getInstance(ex.getCode(), message), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }


}
