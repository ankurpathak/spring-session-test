package com.github.ankurpathak.api.rest.controller;

import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.annotation.CurrentUser;
import com.github.ankurpathak.api.service.IRestControllerService;
import com.github.ankurpathak.api.service.impl.util.ControllerUtil;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.updater.UserUpdaters;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.service.IPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;

import static com.github.ankurpathak.api.constant.ApiPaths.*;

@ApiController
public class PasswordController extends AbstractRestController<User, BigInteger, UserDto> {

    private final CustomUserDetailsService userDetailsService;
    private final IPasswordService service;
    private final PasswordEncoder encoder;

    public PasswordController(CustomUserDetailsService userDetailsService, IPasswordService service, PasswordEncoder encoder, IRestControllerService restControllerService) {
        super(restControllerService);
        this.userDetailsService = userDetailsService;
        this.service = service;
        this.encoder = encoder;
    }


    @Override
    public IDomainService<User, BigInteger> getDomainService() {
        return userDetailsService.getUserService();
    }


    @PutMapping(PATH_FORGET_PASSWORD_EMAIL)
    public ResponseEntity<?> forgetPasswordEmail(@PathVariable(Params.EMAIL) String email, @RequestParam(name = "async", defaultValue = "true") boolean async){
        service.forgotPasswordEmail(email, async);
        return restControllerService.getRestControllerResponseService().processSuccessOk();
    }


    @PutMapping(PATH_FORGET_PASSWORD_ENABLE)
    public ResponseEntity<?> forgetPasswordEnable(@PathVariable(Params.TOKEN) String token) {
        Token.TokenStatus status = service.forgetPasswordEnable(token);
        return restControllerService.getRestControllerResponseService().processTokenStatus(status, token);
    }


    @PutMapping(PATH_FORGET_PASSWORD)
    public ResponseEntity<?> forgetPassword(HttpServletRequest request, @CurrentUser User user, @RequestBody @Validated({UserDto.ForgetPassword.class}) UserDto dto, BindingResult result) {
        User newUser = User.getInstance(user);
        return update(dto, newUser, UserUpdaters.forgetPasswordUpdater(), request, result, (rest, tDto) -> {
            dto.encodedPassword(encoder.encode(dto.getPassword()));
        }, (rest, t, tDto) -> {}); // Added Binding Result
    }


    @PatchMapping(PATH_CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(HttpServletRequest request, @CurrentUser User user, @RequestBody @Validated({UserDto.ChangePassword.class}) UserDto dto, BindingResult result){
        User newUser = User.getInstance(user);
        return update(dto, newUser,UserUpdaters.forgetPasswordUpdater(), request, result, (rest, tDto) -> {
            service.validateExistingPassword(newUser, dto);
            dto.encodedPassword(encoder.encode(dto.getPassword()));
        }, (rest, t, tDto) -> {}); //Added Binding Result
    }


}
