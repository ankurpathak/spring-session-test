package com.github.ankurpathak.api.batch.task.exception.handler.impl;

import com.github.ankurpathak.api.batch.task.exception.handler.IExceptionHandler;
import com.github.ankurpathak.api.exception.FoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.rest.controller.dto.ValidationErrorDto;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.util.FoundExceptionUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Map;

public class FoundExceptionHandler implements IExceptionHandler<FoundException> {
    private final IMessageService messageService;

    public FoundExceptionHandler(IMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Map<String, Object> handelException(Exception ex) {
        FoundException fEx =getException(ex);
        ApiCode code = FoundExceptionUtil.processApiCode(fEx);
        ValidationErrorDto validationErrorDto = FoundExceptionUtil.processFounds(fEx);
        return ApiResponse.getInstance(
                code,
                messageService.getMessage(ApiMessages.FOUND_DEFAULT))
                .addExtra("hints", validationErrorDto)
                .addExtra("stackTrace", ExceptionUtils.getStackTrace(ex))
                .getExtras();
    }

    @Override
    public boolean supports(Exception ex) {
        return FoundException.class.isInstance(ex);
    }
}
