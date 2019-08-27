package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.exception.FoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Map;

public interface IRestControllerResponseService {
    ResponseEntity<?> processObject(Object t, Class<?> tType, String tTypeName, String id);

    ResponseEntity<?> processSuccess(HttpStatus code, Map<String, Object> extras);

    ResponseEntity<?> processSuccessOk();

    ResponseEntity<?> processSuccessOk(Map<String, Object> extras);

    void processValidation(BindingResult result);

    void processValidationForFound(FoundException ex);

    void processNotAllowed(ApiCode code);

    ResponseEntity<?> processTokenStatus(Token.TokenStatus status, String token);

    ResponseEntity<?> processSuccessAccepted(Map<String, Object> extras);
}
