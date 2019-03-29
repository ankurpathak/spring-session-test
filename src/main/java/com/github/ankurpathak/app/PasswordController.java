package com.github.ankurpathak.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.app.annotation.ApiController;
import com.github.ankurpathak.app.annotation.CurrentUser;
import com.github.ankurpathak.app.config.ControllerUtil;
import com.github.ankurpathak.app.controller.rest.dto.AbstractRestController;
import com.github.ankurpathak.app.controller.rest.dto.ApiCode;
import com.github.ankurpathak.app.domain.updater.DomainUpdaters;
import com.github.ankurpathak.app.service.IDomainService;
import com.github.ankurpathak.app.service.IMessageService;
import com.github.ankurpathak.app.service.IUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Optional;

import static com.github.ankurpathak.app.RequestMappingPaths.*;

@ApiController
public class PasswordController extends AbstractRestController<User, BigInteger, UserDto> {

    private final IUserService service;
    private final DomainUpdaters updaters;

    public PasswordController(ApplicationEventPublisher applicationEventPublisher, IMessageService messageService, ObjectMapper objectMapper, LocalValidatorFactoryBean validator, IUserService service, DomainUpdaters updaters) {
        super(applicationEventPublisher, messageService, objectMapper, validator);
        this.service = service;
        this.updaters = updaters;
    }

    @Override
    public IDomainService<User, BigInteger> getService() {
        return service;
    }


    @PutMapping(PATH_FORGET_PASSWORD_EMAIL)
    public ResponseEntity<?> forgetPasswordEmail(@PathVariable(Params.EMAIL) String email){
        Optional<User> user = service.byEmail(email);
        if (user.isPresent()) {
            service.forgotPasswordEmail(user.get());
            return ControllerUtil.processSuccess(messageService);
        } else {
            throw new NotFoundException(email, Params.EMAIL, User.class.getSimpleName(), ApiCode.NOT_FOUND);
        }
    }


    @PutMapping(PATH_FORGET_PASSWORD_ENABLE)
    public ResponseEntity<?> forgetPasswordEnable(@PathVariable(Params.TOKEN) String token) {
        Token.TokenStatus status = service.forgetPasswordEnable(token);
        return ControllerUtil.processTokenStatus(status, token, messageService);
    }


    @PutMapping(PATH_FORGET_PASSWORD)
    public ResponseEntity<?> forgetPassword(HttpServletRequest request, @CurrentUser User user, @RequestBody @Validated({UserDto.ForgetPassword.class}) UserDto dto, BindingResult result) {
        ControllerUtil.processValidation(result, messageService);
        return update(dto, user, updaters.forgetPasswordUpdater(), request);
    }


    @PatchMapping(PATH_CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(HttpServletRequest request, @CurrentUser User user, @RequestBody @Validated({UserDto.ChangePassword.class}) UserDto dto, BindingResult result){
        ControllerUtil.processValidation(result, messageService);
        service.validateExistingPassword(user, dto);
        return update(dto, user, updaters.forgetPasswordUpdater(), request);
    }


}
