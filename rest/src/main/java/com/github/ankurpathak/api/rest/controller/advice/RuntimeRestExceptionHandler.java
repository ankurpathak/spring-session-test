package com.github.ankurpathak.api.rest.controller.advice;

import com.github.ankurpathak.api.exception.FoundException;
import com.github.ankurpathak.api.exception.InvalidException;
import com.github.ankurpathak.api.exception.NotAllowedException;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.util.LogUtil;
import com.github.ankurpathak.api.util.MessageUtil;
import cz.jirutka.rsql.parser.RSQLParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.valid4j.errors.ContractViolation;
import org.valid4j.errors.EnsureViolation;
import org.valid4j.errors.RequireViolation;


@RestControllerAdvice
public class RuntimeRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RuntimeRestExceptionHandler.class);


    private final MessageSource messageSource;

    public RuntimeRestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex, WebRequest request) {
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
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
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
        return handleExceptionInternal(
                ex,
                ApiResponse.getInstance(
                        ex.getFound().getCode(),
                        MessageUtil.getMessage(messageSource, ApiMessages.FOUND, ex.getFound().getEntity(), ex.getFound().getProperty(), ex.getFound().getId())
                ),
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request
        );
    }


    @ExceptionHandler({NotAllowedException.class})
    public ResponseEntity<?> handleNotAllowedException(NotAllowedException ex, WebRequest request) {
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
        return handleExceptionInternal(
                new Exception(ex.getMessage(), ex),
                ApiResponse.getInstance(
                        ex.getApiCode(),
                        MessageUtil.getMessage(messageSource, ApiMessages.NOT_ALLOWED)
                ),
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request
        );
    }


    @ExceptionHandler({EnsureViolation.class, RequireViolation.class})
    public ResponseEntity<?> handleFoundException(ContractViolation ex, WebRequest request) {
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
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
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
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

    @ExceptionHandler({InvalidException.class})
    public ResponseEntity<?> handleInvalidTokenException(InvalidException ex, WebRequest request) {
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
        return handleExceptionInternal(
                ex, ApiResponse.getInstance(
                        ex.getCode(),
                        MessageUtil.getMessage(messageSource, ApiMessages.INVALID, ex.getProperty(), ex.getValue())
                ),
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request
        );
    }

    @ExceptionHandler({RSQLParserException.class})
    public ResponseEntity<?> handleRSQLParserException(RSQLParserException ex, WebRequest request) {
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
        return handleExceptionInternal(
                ex, ApiResponse.getInstance(
                        ApiCode.INVALID_RSQL,
                        MessageUtil.getMessage(messageSource, ApiMessages.INVALID_RSQL)
                ),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
        return handleExceptionInternal(
                ex, ApiResponse.getInstance(
                        ApiCode.INVALID_JSON,
                        MessageUtil.getMessage(messageSource, ApiMessages.INVALID_JSON)
                ),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }








    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
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
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
        return handleExceptionInternal(
                ex,
                ApiResponse.getInstance(ApiCode.REQUIRED_QUERY_PARAM, MessageUtil.getMessage(messageSource, ApiMessages.REQUIRED_QUERY_PARAM, ex.getParameterName())),
                new HttpHeaders(),
                status,
                request
        );
    }

}
