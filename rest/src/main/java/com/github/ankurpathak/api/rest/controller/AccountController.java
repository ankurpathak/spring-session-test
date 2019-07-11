package com.github.ankurpathak.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.config.ControllerUtil;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.constant.RequestMappingPaths;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.domain.converter.DomainConverters;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.event.RegistrationCompleteEvent;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.service.IUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.Map;

@ApiController
public class AccountController extends AbstractRestController<User, BigInteger, UserDto> {
    private final IUserService service;
    private final DomainConverters converters;
    private final PasswordEncoder passwordEncoder;

    @Override
    public IDomainService<User, BigInteger> getUserService() {
        return service;
    }

    public AccountController(ApplicationEventPublisher applicationEventPublisher, IMessageService messageService, ObjectMapper objectMapper, LocalValidatorFactoryBean validator, IUserService service, DomainConverters converters, PasswordEncoder passwordEncoder) {
        super(applicationEventPublisher, messageService, objectMapper, validator);
        this.service = service;
        this.converters = converters;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping(RequestMappingPaths.PATH_ACCOUNT)
    public ResponseEntity<?> account(@RequestParam(name = "async", defaultValue = "true") Boolean async, HttpServletRequest request, HttpServletResponse response, @Validated({UserDto.Default.class, UserDto.Register.class}) @RequestBody UserDto dto, BindingResult result) {
        try {
            dto.password(passwordEncoder.encode(dto.getPassword()));
            User user = tryCreateOne(dto, result, response, converters.userDtoRegisterToDomain());
            applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user, async));
            return ControllerUtil.processSuccessCreated(messageService, Map.of(Params.ID, user.getId()));
        } catch (DuplicateKeyException ex) {
            catchCreateOne(dto, ex, result, request);
            throw ex;
        }
    }


    @PutMapping(RequestMappingPaths.PATH_ACCOUNT_EMAIL)
    public ResponseEntity<?> accountEnableEmail(@PathVariable(Params.Path.EMAIL) String email, @RequestParam(name = "async", defaultValue = "true") boolean async) {
       service.accountEnableEmail(email, async);
       return ControllerUtil.processSuccess(messageService);
    }


    @PutMapping(RequestMappingPaths.PATH_ACCOUNT_ENABLE)
    public ResponseEntity<?> accountEnable(HttpServletRequest request, @PathVariable(Params.Path.TOKEN) String token) {
        Token.TokenStatus status = service.accountEnable(token);
        return ControllerUtil.processTokenStatus(status, token, messageService);
    }


}
