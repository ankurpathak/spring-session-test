package com.github.ankurpathak.api.rest.controller.advice;

import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.exception.*;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.util.LogUtil;
import cz.jirutka.rsql.parser.RSQLParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.valid4j.errors.ContractViolation;
import org.valid4j.errors.EnsureViolation;
import org.valid4j.errors.RequireViolation;
import com.opencsv.exceptions.CsvException;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RuntimeRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RuntimeRestExceptionHandler.class);


    private final IMessageService messageService;

    public RuntimeRestExceptionHandler(IMessageService messageService) {
        this.messageService = messageService;
    }


    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex, WebRequest request) {
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
        return handleExceptionInternal(
                ex,
                ApiResponse.getInstance(
                        ex.getCode(),
                        messageService.getMessage(ApiMessages.NOT_FOUND, ex.getEntity(), ex.getProperty(), ex.getId())
                ),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }


    @ExceptionHandler({TooManyException.class})
    public ResponseEntity<?> handleTooManyException(TooManyException ex, WebRequest request) {
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
        return handleExceptionInternal(
                ex,
                ApiResponse.getInstance(
                        ApiCode.TOO_MANY,
                        messageService.getMessage(ApiMessages.TOO_MANY)
                ).addExtra(Params.ID, ex.getId()),
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
                        messageService.getMessage(ApiMessages.NOT_ALLOWED)
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
                        messageService.getMessage(ApiMessages.INCORRECT_STATE)
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
                        messageService.getMessage(ApiMessages.FOUND_DEFAULT)
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
                        messageService.getMessage(ApiMessages.INVALID, ex.getProperty(), ex.getValue())
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
                        messageService.getMessage(ApiMessages.INVALID_RSQL)
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
                        messageService.getMessage(ApiMessages.INVALID_JSON)
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
                        messageService.getMessage(ApiMessages.BAD_REQUEST)
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
                ApiResponse.getInstance(ApiCode.REQUIRED_QUERY_PARAM, messageService.getMessage(ApiMessages.REQUIRED_QUERY_PARAM, ex.getParameterName())),
                new HttpHeaders(),
                status,
                request
        );
    }


    @ExceptionHandler({FoundException.class})
    public ResponseEntity<?> handleFoundException(FoundException ex, WebRequest request) {
        FoundExceptionHandler handler = new FoundExceptionHandler(messageService);
        return handler.handleFoundException(ex, request, this);
    }

    @ExceptionHandler({CsvException.class})
    public ResponseEntity<?> handleCsvException(CsvException ex, WebRequest request) {
        CsvExceptionHandler handler = new CsvExceptionHandler(messageService);
        return handler.handleCsvException(ex, request, this);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request){
        ValidationExceptionHandler handler =  new ValidationExceptionHandler(messageService);
        return handler.handleValidationException(ex, request, this);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationExceptionHandler handler =  new ValidationExceptionHandler(messageService);
        return handler.handleBindException(ex, headers, status, request, this);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationExceptionHandler handler =  new ValidationExceptionHandler(messageService);
        return handler.handleMethodArgumentNotValid(ex, headers, status, request, this);
    }

    public ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

}
