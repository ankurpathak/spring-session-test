package com.ankurpathak.springsessiontest.controller;

import com.ankurpathak.springsessiontest.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private final ITokenService tokenService;
    private final IEmailService emailService;

    @Override
    public IDomainService<User, BigInteger> getService() {
        return service;
    }

    public AccountRestController(ApplicationEventPublisher applicationEventPublisher, MessageSource messageSource, IUserService service, ITokenService tokenService, IEmailService emailService) {
        super(applicationEventPublisher, messageSource);
        this.service = service;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }


    @PostMapping(PATH_ACCOUNT)
    public ResponseEntity<?> account(HttpServletRequest request, HttpServletResponse response, @Validated({UserDto.Default.class, UserDto.Register.class}) @RequestBody UserDto dto, BindingResult result) {
        try {
            User user = tryCreateOne(dto, result, request, response, UserDto.Register.class);
            applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user));
            return ControllerUtil.processSuccessCreated(messageSource, request, Map.of(ID, user.getId()));
        } catch (DuplicateKeyException ex) {
            catchCreateOne(dto, ex, result, request);
            throw ex;
        }
    }


    @PutMapping(PATH_ACCOUNT_EMAIL)
    public ResponseEntity<?> accountEnableEmail(HttpServletRequest request, @PathVariable(EMAIL) String email) {
        Optional<User> user = service.byEmail(email);
        if (user.isPresent()) {
            service.accountEnableEmail(user.get(), email);
            return ControllerUtil.processSuccess(messageSource, request);
        } else {
            throw new NotFoundException(email, EMAIL, User.class.getSimpleName(), ApiCode.NOT_FOUND);
        }

    }


    @PutMapping(PATH_ACCOUNT_ENABLE)
    public ResponseEntity<?> accountEnable(HttpServletRequest request, @PathVariable(TOKEN) String token) {
        Token.TokenStatus status = service.accountEnable(token);
        return ControllerUtil.processTokenStatus(status, token, messageSource, request);
    }


}
