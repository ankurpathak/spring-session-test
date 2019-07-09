package com.github.ankurpathak.api.controller.rest;

import com.github.ankurpathak.api.config.ControllerUtil;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.annotation.ApiController;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import static com.github.ankurpathak.api.constant.RequestMappingPaths.PATH_GET_ERROR;
import static com.github.ankurpathak.api.constant.RequestMappingPaths.apiPath;


@ApiController
public class ApiErrorController extends AbstractErrorController {

    private final IMessageService messageService;


    public ApiErrorController(ErrorAttributes errorAttributes, IMessageService messageService) {
        super(errorAttributes);

        this.messageService = messageService;
    }

    @Override
    public String getErrorPath() {
        return apiPath(PATH_GET_ERROR);
    }


    @GetMapping(PATH_GET_ERROR)
    public ResponseEntity<?> error(HttpServletRequest request) {
        Map<String, Object> errorAttributes = this.getErrorAttributes(request, false);
        return ControllerUtil.processError(messageService, errorAttributes);
    }

}
