package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.exception.FoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.service.IRestControllerResponseService;
import com.github.ankurpathak.api.service.impl.util.ControllerUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.Map;

@Service
public class RestControllerResponseService implements IRestControllerResponseService {
    private final IMessageService messageService;

    public RestControllerResponseService(IMessageService messageService) {
        this.messageService = messageService;
    }


    @Override
    public ResponseEntity<?> processObject(Object t, Class<?> tType, String tTypeName, String id){
        return ControllerUtil.processObject(t, tType, tTypeName, id, messageService);
    }

    @Override
    public ResponseEntity<?> processSuccess(HttpStatus code, Map<String, Object> extras){
        return ControllerUtil.processSuccess(messageService, code, extras);
    }

    @Override
    public ResponseEntity<?> processSuccessOk(){
        return processSuccess(HttpStatus.OK, Collections.emptyMap());
    }

    @Override
    public ResponseEntity<?> processSuccessOk(Map<String, Object> extras){
        return processSuccess(HttpStatus.OK, extras);
    }

    @Override
    public void processValidation(BindingResult result){
        ControllerUtil.processValidation(result, messageService);
    }

    @Override
    public void processValidationForFound(FoundException ex){
        ControllerUtil.processValidationForFound(ex);
    }

    @Override
    public void processNotAllowed(ApiCode code){
        ControllerUtil.processNotAllowed(messageService, code);
    }

    @Override
    public ResponseEntity<?> processTokenStatus(Token.TokenStatus status, String token) {
        return ControllerUtil.processTokenStatus(status, token, this.messageService);
    }

    @Override
    public ResponseEntity<?> processSuccessAccepted(Map<String, Object> extras) {
        return ControllerUtil.processSuccess(messageService, HttpStatus.ACCEPTED, extras);
    }

}
