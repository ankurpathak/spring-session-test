package com.github.ankurpathak.api.rest.controller.advice;


import com.github.ankurpathak.api.exception.ValidationException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.rest.controller.dto.ValidationErrorDto;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.util.ValidationExceptionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

public class ValidationExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ValidationExceptionHandler.class);


    private final IMessageService messageService;

    public ValidationExceptionHandler(IMessageService messageService) {
        this.messageService = messageService;
    }




    //@ExceptionHandler({ValidationException.class})
    public ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request, RuntimeRestExceptionHandler advice) {
        log.info("message: {} cause: {}", ex.getMessage(), ex.getCause());
        return handleValidationErrors(ex, request,advice);
    }



    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request, RuntimeRestExceptionHandler advice) {
        log.info("message: {} cause: {}", ex.getMessage(), ex.getCause());
        return handleValidationErrors(ex, request, advice);
    }


    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request, RuntimeRestExceptionHandler advice) {
        log.info("message: {} cause: {}", ex.getMessage(), ex.getCause());
        return handleValidationErrors(ex, request, advice);
    }


    private ResponseEntity<Object> handleValidationErrors(Exception ex, WebRequest request, RuntimeRestExceptionHandler advice) {
        String[] messages = {};
        ApiCode code = null;
        List<BindingResult> results = new ArrayList<>();
        if (BindException.class.isAssignableFrom(ex.getClass())) {
            results.add(((BindException) ex).getBindingResult());
        } else if (MethodArgumentNotValidException.class.isAssignableFrom(ex.getClass())) {
            results.add(((MethodArgumentNotValidException) ex).getBindingResult());
        } else if (ex instanceof ValidationException) {
            ValidationException vEx = (ValidationException) ex;
            results.addAll(vEx.getBindingResults());
            messages = ArrayUtils.addAll(messages, vEx.getMessages());
            code = vEx.getCode();
        }
        if(ArrayUtils.isEmpty(messages))
            messages = ArrayUtils.add(messages, messageService.getMessage(ApiMessages.VALIDATION));
        if(code == null)
            code = ApiCode.VALIDATION;

        ApiResponse dto = ApiResponse.getInstance(code, messages);
        ValidationErrorDto mainDto = ValidationErrorDto.getInstance();
        if (CollectionUtils.isNotEmpty(results)) {
            for(BindingResult result: results){
                List<FieldError> fieldErrors = result.getFieldErrors();
                ValidationErrorDto validationErrorDto = ValidationExceptionUtil.processFieldErrors(messageService, fieldErrors);
                List<ObjectError> objectErrors = result.getGlobalErrors();
                List<String> starErrors = ValidationExceptionUtil.processGlobalErrors(messageService, objectErrors);
                for (String starError : starErrors) {
                    validationErrorDto.addError("*", starError);
                }
                mainDto.addErrors(validationErrorDto.getErrors());
            }

            dto.addExtra("hints", mainDto);

        }
        return advice.handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }


}


