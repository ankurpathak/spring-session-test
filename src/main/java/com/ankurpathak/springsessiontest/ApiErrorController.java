package com.ankurpathak.springsessiontest;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

import static com.ankurpathak.springsessiontest.RequestMappingPaths.PATH_GET_ERROR;
import static com.ankurpathak.springsessiontest.RequestMappingPaths.apiPath;


@ApiController
public class ApiErrorController extends AbstractErrorController {

    private final MessageSource messageSource;


    public ApiErrorController(ErrorAttributes errorAttributes, MessageSource messageSource) {
        super(errorAttributes);
        this.messageSource = messageSource;
    }

    @Override
    public String getErrorPath() {
        return apiPath(PATH_GET_ERROR);
    }


    @GetMapping(PATH_GET_ERROR)
    public ResponseEntity<?> error(HttpServletRequest request) {
        return ControllerUtil.processError(messageSource, request);
    }

}
