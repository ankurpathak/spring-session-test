package com.ankurpathak.springsessiontest;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Optional;

import static com.ankurpathak.springsessiontest.Params.EMAIL;
import static com.ankurpathak.springsessiontest.Params.TOKEN;
import static com.ankurpathak.springsessiontest.RequestMappingPaths.*;

@ApiController
public class PasswordController extends AbstractRestController<User, BigInteger, UserDto> {

    private final IUserService service;

    public PasswordController(ApplicationEventPublisher applicationEventPublisher, MessageSource messageSource, IUserService service) {
        super(applicationEventPublisher, messageSource);
        this.service = service;
    }

    @Override
    public IDomainService<User, BigInteger> getService() {
        return service;
    }


    @PutMapping(PATH_FORGET_PASSWORD_EMAIL)
    public ResponseEntity<?> forgetPasswordEmail(HttpServletRequest request, @PathVariable(EMAIL) String email){
        Optional<User> user = service.byEmail(email);
        if (user.isPresent()) {
            service.forgotPasswordEmail(user.get(), email);
            return ControllerUtil.processSuccess(messageSource, request);
        } else {
            throw new NotFoundException(email, EMAIL, User.class.getSimpleName(), ApiCode.NOT_FOUND);
        }
    }


    @PutMapping(PATH_FORGET_PASSWORD_ENABLE)
    public ResponseEntity<?> forgetPasswordEnable(HttpServletRequest request, @PathVariable(TOKEN) String token) {
        Token.TokenStatus status = service.forgetPasswordEnable(token);
        return ControllerUtil.processTokenStatus(status, token, messageSource, request);
    }


    @PutMapping(PATH_FORGET_PASSWORD)
    public ResponseEntity<?> forgetPassword(HttpServletRequest request, @CurrentUser User user, @RequestBody @Validated({DomainDto.PasswordReset.class}) UserDto dto, BindingResult result) {
        ControllerUtil.processValidation(result, messageSource, request);
        return update(dto, user, UpdateDomainUpdaters.forgetPasswordUpdater, request);
    }


}
