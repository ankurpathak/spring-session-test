package com.github.ankurpathak.api.rest.controller.advice;

import com.github.ankurpathak.api.exception.FoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.rest.controller.dto.ValidationErrorDto;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.util.FoundExceptionUtil;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

public class FoundExceptionHandler  {

    private static final Logger log = LoggerFactory.getLogger(FoundExceptionHandler.class);

    private final IMessageService messageService;

    public FoundExceptionHandler(IMessageService messageService) {
        this.messageService = messageService;
    }

    //@ExceptionHandler({FoundException.class})
    public ResponseEntity<?> handleFoundException(FoundException ex, WebRequest request, RuntimeRestExceptionHandler advice) {
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
        ApiCode code = FoundExceptionUtil.processApiCode(ex);
        ValidationErrorDto validationErrorDto = FoundExceptionUtil.processFounds(ex);
        return advice.handleExceptionInternal(
                ex,
                ApiResponse.getInstance(
                        code,
                        messageService.getMessage(ApiMessages.FOUND, ex.getEntity(), ex.getFound().getProperty(), ex.getFound().getId())
                ).addExtra("hints", validationErrorDto),
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request
        );
    }
}
