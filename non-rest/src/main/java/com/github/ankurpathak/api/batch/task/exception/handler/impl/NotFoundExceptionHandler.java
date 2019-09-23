package com.github.ankurpathak.api.batch.task.exception.handler.impl;

import com.github.ankurpathak.api.batch.task.CsvTaskContext;
import com.github.ankurpathak.api.batch.task.TaskContextHolder;
import com.github.ankurpathak.api.batch.task.exception.handler.IExceptionHandler;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.service.IMessageService;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotFoundExceptionHandler implements IExceptionHandler<NotFoundException> {
    private final IMessageService messageService;

    public NotFoundExceptionHandler(IMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Map<String, Object> handelException(Exception ex) {
        NotFoundException nEx = getException(ex);
        List<String> hints = new ArrayList<>();
        hints.add(ex.getMessage());


        return ApiResponse.getInstance(
                nEx.getCode(),
                messageService.getMessage(ApiMessages.NOT_FOUND, nEx.getEntity(), nEx.getProperty(), nEx.getId())
        ).addExtra("hints", hints)
                .addExtra("stackTrace", ExceptionUtils.getStackTrace(ex))
                .getExtras();
    }

    @Override
    public boolean supports(Exception ex) {
        return NotFoundException.class.isInstance(ex);
    }
}
