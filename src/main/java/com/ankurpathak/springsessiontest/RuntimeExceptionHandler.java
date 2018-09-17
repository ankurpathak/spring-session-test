package com.ankurpathak.springsessiontest;

import com.ankurpathak.springsessiontest.controller.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.valid4j.errors.EnsureViolation;


@RestControllerAdvice
public class RuntimeExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RuntimeExceptionHandler.class);


    private final MessageSource messageSource;

    public RuntimeExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex, WebRequest request) {
        log.info("message: {} cause: {}", ex.getMessage(), ex.getCause());
        ex.printStackTrace();
        return handleExceptionInternal(
                ex,
                ApiResponse.getInstance(
                        ex.getCode(),
                        MessageUtil.getMessage(messageSource, ApiMessages.NOT_FOUND, ex.getEntity(), ex.getProperty(), ex.getId())
                ),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }


    @ExceptionHandler({FoundException.class})
    public ResponseEntity<?> handleFoundException(FoundException ex, WebRequest request) {
        log.info("message: {} cause: {}", ex.getMessage(), ex.getCause());
        ex.printStackTrace();
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


    @ExceptionHandler({EnsureViolation.class})
    public ResponseEntity<?> handleFoundException(EnsureViolation ex, WebRequest request) {
        log.info("message: {} cause: {}", ex.getMessage(), ex.getCause());
        ex.printStackTrace();
        return handleExceptionInternal(
                new Exception(ex.getMessage(), ex),
                ApiResponse.getInstance(
                        ApiCode.INCORRECT_STATE,
                        MessageUtil.getMessage(messageSource, ApiMessages.INCORRECT_STATE)
                ),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }


    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity<?> handleDuplicateKeyException(DuplicateKeyException ex, WebRequest request) {
        log.info("message: {} cause: {}", ex.getMessage(), ex.getCause());
        ex.printStackTrace();
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

    @ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<?> handleInvalidTokenException(InvalidTokenException ex, WebRequest request) {
        log.info("message: {} cause: {}", ex.getMessage(), ex.getCause());
        return handleExceptionInternal(
                ex, ApiResponse.getInstance(
                        ex.getCode(),
                        ex.getMessage()
                ),
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request
        );
    }


    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("message: {} cause: {}", ex.getMessage(), ex.getCause());
        ex.printStackTrace();
        return handleExceptionInternal(
                ex, ApiResponse.getInstance(
                        ApiCode.BAD_REQUEST,
                        MessageUtil.getMessage(messageSource, ApiMessages.BAD_REQUEST)
                ),
                new HttpHeaders(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                request
        );
    }


    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("message: {} cause: {}", ex.getMessage(), ex.getCause());
        ex.printStackTrace();
        return handleExceptionInternal(
                ex,
                ApiResponse.getInstance(ApiCode.REQUIRED_QUERY_PARAM, MessageUtil.getMessage(messageSource, ApiMessages.REQUIRED_QUERY_PARAM, ex.getParameterName())),
                new HttpHeaders(),
                status,
                request
        );
    }

}
