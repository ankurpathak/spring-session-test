package com.github.ankurpathak.api.batch.task.exception.handler;

import com.github.ankurpathak.api.batch.task.CsvTaskContext;
import com.github.ankurpathak.api.batch.task.TaskContextHolder;
import com.github.ankurpathak.api.domain.repository.dto.FileContext;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.service.IMessageService;
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
    public Map<String, Object> handelException(RuntimeException ex) {
        List<String> hints = new ArrayList<>();
        hints.add(ex.getMessage());
        hints.add(ex.getCause().getMessage());
        return ApiResponse.getInstance(
                ApiCode.INVALID_CSV,
                messageService.getMessage(ApiMessages.INVALID, "Csv", TaskContextHolder.getContext().filter(CsvTaskContext.class::isInstance).map(CsvTaskContext.class::cast).flatMap(CsvTaskContext::getFile).map(FileContext::getFileName).orElse("")))
        .addExtra("hints", hints)
                .getExtras();
    }


}
