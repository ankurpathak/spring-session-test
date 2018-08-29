package com.ankurpathak.springsessiontest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
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
        return handleExceptionInternal(
                ex,
                ApiResponse.getInstance(
                        ex.getCode(),
                        MessageUtil.getMessage(messageSource,ApiMessages.NOT_FOUND, ex.getEntity(), ex.getProperty(), ex.getId())
                ),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }


    @ExceptionHandler({FoundException.class})
    public ResponseEntity<Object> handleFoundException(FoundException ex, WebRequest request) {
        return handleExceptionInternal(
                ex,
                ApiResponse.getInstance(
                        ex.getCode(),
                        MessageUtil.getMessage(messageSource, ApiMessages.FOUND, ex.getEntity(), ex.getProperty(), ex.getId())
                ),
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request
        );
    }

    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException ex, WebRequest request) {
        return handleExceptionInternal(
                ex, ApiResponse.getInstance(
                        ApiCode.FOUND,
                        MessageUtil.getMessage(messageSource, ApiMessages.FOUND_DEFAULT)
                ),
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request
        );
    }



}
