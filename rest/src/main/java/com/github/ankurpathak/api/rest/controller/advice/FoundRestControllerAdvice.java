package com.github.ankurpathak.api.rest.controller.advice;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.model.Product;
import com.github.ankurpathak.api.domain.model.User;
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

import java.util.List;

@RestControllerAdvice
public class FoundRestControllerAdvice  extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ValidationRestControllerAdvice.class);

    private final IMessageService messageService;

    public FoundRestControllerAdvice(IMessageService messageService) {
        this.messageService = messageService;
    }


    @ExceptionHandler({FoundException.class})
    public ResponseEntity<?> handleFoundException(FoundException ex, WebRequest request) {
        log.error("{} message: {} cause: {}",ex.getClass().getSimpleName(),  ex.getMessage(), ex.getCause());
        LogUtil.logStackTrace(log, ex);
        List<FoundDto> founds = ex.getFounds();
        ApiCode code = ApiCode.FOUND;
        if(ex.hasOnlyFound()){
            FoundDto dto = ex.getFound();
            if(StringUtils.equalsIgnoreCase(ex.getEntity(), User.class.getSimpleName())){
                if(StringUtils.equalsIgnoreCase(dto.getProperty(), Model.User.Field.EMAIL_VALUE)){
                    code = ApiCode.EMAIL_FOUND;
                }else if(StringUtils.equalsIgnoreCase(dto.getProperty(), Model.User.Field.USERNAME)){
                    code = ApiCode.USERNAME_FOUND;
                }else if(StringUtils.equalsIgnoreCase(dto.getProperty(), Model.User.Field.PHONE_VALUE)){
                    code = ApiCode.PHONE_FOUND;
                }
            }else if(StringUtils.equalsIgnoreCase(ex.getEntity(), Product.class.getSimpleName())){
                if(StringUtils.equalsIgnoreCase(dto.getProperty(), Model.Product.Field.NAME)){
                }
            }
        }
        ValidationErrorDto validationErrorDto = ValidationErrorDto.getInstance();
        for(FoundDto dto: founds){
            validationErrorDto.addError(dto.getProperty(), dto.getId());
        }



        return handleExceptionInternal(
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
