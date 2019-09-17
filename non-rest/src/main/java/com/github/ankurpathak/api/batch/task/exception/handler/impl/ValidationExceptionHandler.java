package com.github.ankurpathak.api.batch.task.exception.handler.impl;

import com.github.ankurpathak.api.batch.task.exception.handler.IExceptionHandler;
import com.github.ankurpathak.api.exception.ValidationException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.rest.controller.dto.ValidationErrorDto;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.util.ValidationExceptionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ValidationExceptionHandler implements IExceptionHandler<ValidationException> {
    private final IMessageService messageService;

    public ValidationExceptionHandler(IMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Map<String, Object> handelException(ValidationException ex) {
        String[] messages = {};
        ApiCode code = null;
        List<BindingResult> results = new ArrayList<>(ex.getBindingResults());
        messages = ArrayUtils.addAll(messages, ex.getMessages());
        code = ex.getCode();

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

            dto.addExtra("hints", mainDto)
                    .addExtra("stackTrace", ExceptionUtils.getStackTrace(ex));


        }
        return dto.getExtras();
    }
}
