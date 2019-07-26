package com.github.ankurpathak.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.config.ControllerUtil;
import com.github.ankurpathak.api.constant.ApiPaths;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.converter.UserConverters;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.event.RegistrationCompleteEvent;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.service.IAccountService;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.service.IUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;
import java.math.BigInteger;

@ApiController
public class AccountController extends AbstractRestController<User, BigInteger, UserDto> {
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final IAccountService service;

    @Override
    public IDomainService<User, BigInteger> getDomainService() {
        return userService;
    }

    public AccountController(ApplicationEventPublisher applicationEventPublisher, IMessageService messageService, ObjectMapper objectMapper, LocalValidatorFactoryBean validator, IUserService userService, PasswordEncoder passwordEncoder, IAccountService service) {
        super(applicationEventPublisher, messageService, objectMapper, validator);
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.service = service;
    }


    @PostMapping(ApiPaths.PATH_ACCOUNT)
    public ResponseEntity<?> account(HttpServletRequest request, HttpServletResponse response, @Validated({Default.class, UserDto.Account.class}) @RequestBody UserDto dto, BindingResult result) {
        return createOne(dto, result, request, response, UserConverters.createAccount(),
                (rest, tDto) -> {
                    tDto.encodedPassword(passwordEncoder.encode(tDto.getPassword()));
                }, (rest, t, tDto) -> {
                    applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(t));
                });
    }


    @PutMapping(ApiPaths.PATH_ACCOUNT_EMAIL)
    public ResponseEntity<?> accountEnableEmail(@PathVariable(Params.Path.EMAIL) String email) {
        service.accountEnableEmail(email);
        return ControllerUtil.processSuccess(messageService);
    }


    @PutMapping(ApiPaths.PATH_ACCOUNT_ENABLE)
    public ResponseEntity<?> accountEnable(HttpServletRequest request, @PathVariable(Params.Path.TOKEN) String token) {
        Token.TokenStatus status = service.accountEnable(token);
        return ControllerUtil.processTokenStatus(status, token, messageService);
    }


}
