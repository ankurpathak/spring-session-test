package com.github.ankurpathak.api.rest.controller.advice;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.exception.CsvException;
import com.github.ankurpathak.api.exception.FoundException;
import com.github.ankurpathak.api.exception.dto.FoundDto;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.rest.controller.dto.ValidationErrorDto;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CsvRestControllerAdvice extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ValidationRestControllerAdvice.class);

    private final IMessageService messageService;

    public CsvRestControllerAdvice(IMessageService messageService) {
        this.messageService = messageService;
    }


    @ExceptionHandler({CsvException.class})
    public ResponseEntity<?> handleCsvException(CsvException ex, WebRequest request) {
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);

        List<String> hints = new ArrayList<>();

        if(RuntimeException.class.isAssignableFrom(ex.getCause().getClass())){
            hints.add(ex.getCause().getMessage());
        }

        return handleExceptionInternal(
                ex,
                ApiResponse.getInstance(
                        ApiCode.INVALID_CSV,
                        messageService.getMessage(ApiMessages.INVALID, "Csv", ex.getFile() )
                ).addExtra("hints", hints),
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request
        );
    }
}
