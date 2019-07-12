package com.github.ankurpathak.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.annotation.CurrentUser;
import com.github.ankurpathak.api.config.ControllerUtil;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.updater.DomainUpdaters;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IMessageService;
import com.github.ankurpathak.api.service.IPasswordService;
import com.github.ankurpathak.api.service.IUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;

import static com.github.ankurpathak.api.constant.RequestMappingPaths.*;

@ApiController
public class PasswordController extends AbstractRestController<User, BigInteger, UserDto> {

    private final IUserService userService;
    private final IPasswordService service;
    private final DomainUpdaters updaters;
    private final PasswordEncoder encoder;

    public PasswordController(ApplicationEventPublisher applicationEventPublisher, IMessageService messageService, ObjectMapper objectMapper, LocalValidatorFactoryBean validator, IPasswordService service, IUserService userService, DomainUpdaters updaters, PasswordEncoder encoder) {
        super(applicationEventPublisher, messageService, objectMapper, validator);
        this.service = service;
        this.userService = userService;
        this.updaters = updaters;
        this.encoder = encoder;
    }

    @Override
    public IDomainService<User, BigInteger> getUserService() {
        return userService;
    }


    @PutMapping(PATH_FORGET_PASSWORD_EMAIL)
    public ResponseEntity<?> forgetPasswordEmail(@PathVariable(Params.EMAIL) String email, @RequestParam(name = "async", defaultValue = "true") boolean async){
        service.forgotPasswordEmail(email, async);
        return ControllerUtil.processSuccess(messageService);
    }


    @PutMapping(PATH_FORGET_PASSWORD_ENABLE)
    public ResponseEntity<?> forgetPasswordEnable(@PathVariable(Params.TOKEN) String token) {
        Token.TokenStatus status = service.forgetPasswordEnable(token);
        return ControllerUtil.processTokenStatus(status, token, messageService);
    }


    @PutMapping(PATH_FORGET_PASSWORD)
    public ResponseEntity<?> forgetPassword(HttpServletRequest request, @CurrentUser User user, @RequestBody @Validated({UserDto.ForgetPassword.class}) UserDto dto, BindingResult result) {
        ControllerUtil.processValidation(result, messageService);
        dto.encodedPassword(encoder.encode(dto.getPassword()));
        return update(dto, user, updaters.forgetPasswordUpdater(), request);
    }


    @PatchMapping(PATH_CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(HttpServletRequest request, @CurrentUser User user, @RequestBody @Validated({UserDto.ChangePassword.class}) UserDto dto, BindingResult result){
        ControllerUtil.processValidation(result, messageService);
        service.validateExistingPassword(user, dto);
        dto.encodedPassword(encoder.encode(dto.getPassword()));
        return update(dto, user, updaters.forgetPasswordUpdater(), request);
    }


}
