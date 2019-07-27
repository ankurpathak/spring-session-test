package com.github.ankurpathak.api.config;

import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.exception.*;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controller.dto.ApiMessages;
import com.github.ankurpathak.api.rest.controller.dto.ApiResponse;
import com.github.ankurpathak.api.service.IMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class ControllerUtil {

    private static void processValidation(BindingResult result, IMessageService messageService, ApiCode code, String message) {
        if (result.hasErrors()) {
            throw new ValidationException(
                    Collections.singletonList(result),
                    code,
                    messageService.getMessage(message)
            );
        }
    }

    public static void processNotAllowed(IMessageService messageService, ApiCode code){
        throw new NotAllowedException(code);
    }

    @SuppressWarnings("all")
    public static <T> ResponseEntity<T> processOptional(Optional<T> t, Class<T> type, String typeNeme, String id, IMessageService messageService) {
        return t.map(ResponseEntity::ok).orElseThrow(() -> new NotFoundException(String.valueOf(id), Params.ID, typeNeme != null ? typeNeme: type.getSimpleName(), ApiCode.NOT_FOUND));

    }


    public static void processValidation(BindingResult result, IMessageService messageService) {
        processValidation(result, messageService, ApiCode.VALIDATION, ApiMessages.VALIDATION);
    }


    public static void processValidationForFound(IMessageService messageService, FoundException ex) {
        if (ex.hasErrors()) {
            throw new ValidationException(
                    ex.getBindingResults(),
                    ex.getFound().getCode(),
                    messageService.getMessage(ApiMessages.FOUND, ex.getFound().getEntity(), ex.getFound().getProperty(), ex.getFound().getId())
                    );
        }
    }

    public static ResponseEntity<?> processError(IMessageService messageService, Map<String, Object> extras) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponse.getInstance(
                                ApiCode.UNKNOWN,
                                messageService.getMessage(ApiMessages.UNKNOWN)
                        )
                                .addExtras(extras)
                );

    }


    public static ResponseEntity<?> processSuccess(IMessageService messageService, HttpStatus code, Map<String, Object> extras) {
        return ResponseEntity.status(code)
                .body(
                        ApiResponse.getInstance(
                                ApiCode.SUCCESS,
                                messageService.getMessage(ApiMessages.SUCCESS)
                        ).addExtras(extras)
                );
    }


    public static ResponseEntity<?> processSuccessNoContent() {
        return ResponseEntity.noContent().build();
    }


    public static ResponseEntity<?> processSuccess(IMessageService messageService) {
        return processSuccess(messageService, HttpStatus.OK, Collections.emptyMap());
    }

    public static ResponseEntity<?> processSuccess(IMessageService messageService, Map<String, Object> extras) {
        return processSuccess(messageService, HttpStatus.OK, extras);
    }

    public static ResponseEntity<?> processSuccessCreated(IMessageService messageService) {
        return processSuccess(messageService, HttpStatus.CREATED, Collections.emptyMap());
    }

    public static ResponseEntity<?> processSuccessCreated(IMessageService messageService, Map<String, Object> extras) {
        return processSuccess(messageService, HttpStatus.CREATED, extras);
    }


    public static ResponseEntity<?> processTokenStatus(Token.TokenStatus status, String token, IMessageService messageService) {
        switch (status) {
            case VALID:
                return ControllerUtil.processSuccess(messageService);

            case EXPIRED:
                return ControllerUtil.processExpiredToken(token, messageService);
            case INVALID:
            default:
                throw new InvalidException(ApiCode.INVALID_TOKEN, Params.TOKEN, token);
        }
    }

    public static ResponseEntity<?> processExpiredToken(String token, IMessageService messageService) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponse.getInstance(
                        ApiCode.EXPIRED_TOKEN,
                        messageService.getMessage(ApiMessages.EXPIRED_TOKEN, token)
                )
        );
    }


    public static final String PATTERN_EXACT = "^%s$";


}
