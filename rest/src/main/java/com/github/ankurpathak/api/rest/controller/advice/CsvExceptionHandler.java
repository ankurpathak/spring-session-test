package com.github.ankurpathak.api.rest.controller.advice;

import com.github.ankurpathak.api.exception.CsvException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

public class CsvExceptionHandler{

    private static final Logger log = LoggerFactory.getLogger(CsvExceptionHandler.class);

    private final IMessageService messageService;

    public CsvExceptionHandler(IMessageService messageService) {
        this.messageService = messageService;
    }


    //@ExceptionHandler({CsvException.class})
    public ResponseEntity<?> handleCsvException(CsvException ex, WebRequest request, RuntimeRestExceptionHandler advice) {
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);

        List<String> hints = new ArrayList<>();

        if(RuntimeException.class.isAssignableFrom(ex.getCause().getClass())){
            hints.add(ex.getCause().getMessage());
        }

        return advice.handleExceptionInternal(
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
