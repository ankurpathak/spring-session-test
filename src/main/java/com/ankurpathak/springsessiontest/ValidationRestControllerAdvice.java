package com.ankurpathak.springsessiontest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
import co.indexify.dto.ResponseCode;
import co.indexify.dto.ResponseDto;
import co.indexify.dto.ResponseMessage;
import co.indexify.dto.ValidationErrorDto;
import co.indexify.exception.ExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice(basePackageClasses = MeController.class)
public class ValidationRestControllerAdvice extends ResponseEntityExceptionHandler {


    @Autowired
    private MessageSource messageSource;


    private ValidationErrorDto processFieldErrors(List<FieldError> fieldErrors) {
        ValidationErrorDto validationErrorDto = new ValidationErrorDto();
        for (FieldError fieldError : fieldErrors) {
            String localizedErrorMessage = resolveLocalizedFieldErrorMessage(fieldError);
            validationErrorDto.addError(fieldError.getField(), localizedErrorMessage);
        }
        return validationErrorDto;
    }


    private String resolveLocalizedFieldErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
        //If the message was not found, return the most accurate field error code instead.
        //You can remove this check if you prefer to get the default error message.
        if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
            String[] fieldErrorCodes = fieldError.getCodes();
            localizedErrorMessage = fieldErrorCodes[0];
        }


        return localizedErrorMessage;
    }

    private List<String> processGlobalErrors(List<ObjectError> objectErrors) {
        List<String> errors = new ArrayList<>();
        for (ObjectError objectError : objectErrors) {
            String localizedErrorMessage = resolveLocalizedObjectErrorMessage(objectError);
            errors.add(localizedErrorMessage);
        }
        return errors;
    }

    private String resolveLocalizedObjectErrorMessage(ObjectError objectError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(objectError, currentLocale);

        //If the message was not found, return the most accurate field error code instead.
        //You can remove this check if you prefer to get the default error message.
        if (localizedErrorMessage.equals(objectError.getDefaultMessage())) {
            String[] objectErrorCodes = objectError.getCodes();
            localizedErrorMessage = objectErrorCodes[0];
        }

        return localizedErrorMessage;
    }

    @ExceptionHandler({ExistException.class})
    public ResponseEntity<Object> handleExistException(ExistException ex, WebRequest request) {
        logger.error("409 Status Code", ex);
        return handleValidationErrors(ex, request, HttpStatus.CONFLICT);
    }


    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("400 Status Code", ex);
        return handleValidationErrors(ex, request);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("400 Status Code", ex);
        return handleValidationErrors(ex, request);
    }

    private ResponseEntity<Object> handleValidationErrors(Exception ex, WebRequest request){
        return handleValidationErrors(ex, request, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({ ValidationException.class })
    public ResponseEntity<Object> handleUserAlreadyExist(ValidationException ex, WebRequest request) {
        logger.error("409 Status Code", ex);
        return handleValidationErrors(ex, request, HttpStatus.BAD_REQUEST);
    }



    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = messageSource.getMessage(ResponseMessage.MESSAGE_QUERY_PARAM_MISSING, new Object[]{ex.getParameterName()}, request.getLocale());
        ResponseDto dto = ResponseDto.of(message, ResponseCode.QUERY_PARAM_MISSING, status);
        return handleExceptionInternal(ex, dto, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleValidationErrors(Exception ex, WebRequest request, HttpStatus status) {
        String message = messageSource.getMessage(ResponseMessage.MESSAGE_VALIDATION, null, request.getLocale());
        ResponseCode code = ResponseCode.VALIDATION;
        BindingResult result = null;
        if(ex instanceof BindException){
            result = ((BindException)ex).getBindingResult();
        }else if(ex instanceof MethodArgumentNotValidException){
            result = ((MethodArgumentNotValidException)ex).getBindingResult();
        }else if(ex instanceof ValidationException){
            ValidationException vex = (ValidationException)ex;
            result = vex.getBindingResult();
            message  = ex.getMessage();
            code = vex.getResponseCode();
        }else if(ex instanceof ExistException){
            ExistException eex = (ExistException)ex;
            result = eex.getBindingResult();
            code = eex.getResponseCode();
            message  = eex.getMessage();
            if(message == null){
                message = messageSource.getMessage(ResponseMessage.MESSAGE_EXIST, new Object[]{((ExistException)ex).getEntity()}, request.getLocale());
            }
        }
        ResponseDto dto = ResponseDto.of(message, code, status);
        if(result != null){
            List<FieldError> fieldErrors = result.getFieldErrors();
            ValidationErrorDto validationErrorDto = processFieldErrors(fieldErrors);
            List<ObjectError> objectErrors = result.getGlobalErrors();
            List<String> starErrors = processGlobalErrors(objectErrors);
            for (String starError : starErrors) {
                validationErrorDto.addError("*", starError);
            }
            dto.addExtra("validationErrors", validationErrorDto);

        }
        return handleExceptionInternal(ex, dto, new HttpHeaders(), status, request);
    }




}


*/
