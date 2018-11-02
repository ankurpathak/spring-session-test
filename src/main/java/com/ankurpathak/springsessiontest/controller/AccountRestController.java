package com.ankurpathak.springsessiontest.controller;

import com.ankurpathak.springsessiontest.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

import static com.ankurpathak.springsessiontest.Params.*;
import static com.ankurpathak.springsessiontest.RequestMappingPaths.*;

@ApiController
public class AccountRestController extends AbstractRestController<User, BigInteger, UserDto> {
    private final IUserService service;
    private final DomainConverters converters;

    @Override
    public IDomainService<User, BigInteger> getService() {
        return service;
    }

    public AccountRestController(ApplicationEventPublisher applicationEventPublisher, IMessageService messageService, ObjectMapper objectMapper, LocalValidatorFactoryBean validator, IUserService service, DomainConverters converters) {
        super(applicationEventPublisher, messageService, objectMapper, validator);
        this.service = service;

        this.converters = converters;
    }


    @PostMapping(PATH_ACCOUNT)
    public ResponseEntity<?> account(HttpServletRequest request, HttpServletResponse response, @Validated({UserDto.Default.class, UserDto.Register.class}) @RequestBody UserDto dto, BindingResult result) {
        try {
            User user = tryCreateOne(dto, result, response, converters.userDtoRegisterToDomain());
            applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user));
            return ControllerUtil.processSuccessCreated(messageService, Map.of(ID, user.getId()));
        } catch (DuplicateKeyException ex) {
            catchCreateOne(dto, ex, result, request);
            throw ex;
        }
    }


    @PutMapping(PATH_ACCOUNT_EMAIL)
    public ResponseEntity<?> accountEnableEmail(@PathVariable(EMAIL) String email) {
        Optional<User> user = service.byEmail(email);
        if (user.isPresent()) {
            service.accountEnableEmail(user.get(), email);
            return ControllerUtil.processSuccess(messageService);
        } else {
            throw new NotFoundException(email, EMAIL, User.class.getSimpleName(), ApiCode.NOT_FOUND);
        }

    }


    @PutMapping(PATH_ACCOUNT_ENABLE)
    public ResponseEntity<?> accountEnable(HttpServletRequest request, @PathVariable(TOKEN) String token) {
        Token.TokenStatus status = service.accountEnable(token);
        return ControllerUtil.processTokenStatus(status, token, messageService);
    }


}
