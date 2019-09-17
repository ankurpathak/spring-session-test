package com.github.ankurpathak.api.batch.task.exception.handler.impl;

import com.github.ankurpathak.api.batch.task.CsvTaskContext;
import com.github.ankurpathak.api.batch.task.TaskContextHolder;
import com.github.ankurpathak.api.batch.task.exception.handler.IExceptionHandler;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.service.IMessageService;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CsvExceptionHandler implements IExceptionHandler<RuntimeException> {
    private final IMessageService messageService;

    public CsvExceptionHandler(IMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Map<String, Object> handelException(Exception ex) {
        List<String> hints = new ArrayList<>();
        hints.add(ex.getMessage());
        hints.add(ex.getCause().getMessage());
        return ApiResponse.getInstance(
                ApiCode.INVALID_CSV,
                messageService.getMessage(ApiMessages.INVALID, "Csv", TaskContextHolder.getContext().filter(CsvTaskContext.class::isInstance).map(CsvTaskContext.class::cast).flatMap(CsvTaskContext::getFile).map(FileContext::getFileName).orElse("")))
                .addExtra("hints", hints)
                .addExtra("stackTrace", ExceptionUtils.getStackTrace(ex))
                .getExtras();
    }

    @Override
    public boolean supports(Exception ex) {
        return RuntimeException.class.isInstance(ex) && CsvException.class.isInstance(ex.getCause());
    }


}
