package com.github.ankurpathak.api.batch.task.exception.handler.impl;

import com.github.ankurpathak.api.batch.task.exception.handler.IExceptionHandler;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.service.IMessageService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DuplicateKeyExceptionHandler implements IExceptionHandler<DuplicateKeyException> {
    private final IMessageService messageService;

    public DuplicateKeyExceptionHandler(IMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Map<String, Object> handelException(Exception ex) {
        List<String> hints = new ArrayList<>();
        hints.add(ex.getMessage());
        return ApiResponse.getInstance(
                ApiCode.FOUND,
                messageService.getMessage(ApiMessages.FOUND_DEFAULT))
                .addExtra("hints", hints)
                .addExtra("stackTrace", ExceptionUtils.getStackTrace(ex))
                .getExtras();
    }

    @Override
    public boolean supports(Exception ex) {
        return DuplicateKeyException.class.isInstance(ex);
    }
}
