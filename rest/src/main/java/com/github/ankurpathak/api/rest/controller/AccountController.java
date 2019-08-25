package com.github.ankurpathak.api.rest.controller;

import com.github.ankurpathak.api.annotation.ApiController;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.IRestControllerService;
import com.github.ankurpathak.api.service.impl.util.ControllerUtil;
import com.github.ankurpathak.api.constant.ApiPaths;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.converter.UserConverters;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.event.RegistrationCompleteEvent;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.service.IAccountService;
import com.github.ankurpathak.api.service.IDomainService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final IAccountService service;

    public AccountController(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, IAccountService service, IRestControllerService restControllerService) {
        super(restControllerService);
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.service = service;
    }

    @Override
    public IDomainService<User, BigInteger> getDomainService() {
        return userDetailsService.getUserService();
    }




    @PostMapping(ApiPaths.PATH_ACCOUNT)
    public ResponseEntity<?> account(HttpServletRequest request, HttpServletResponse response, @Validated({Default.class, UserDto.Account.class}) @RequestBody UserDto dto, BindingResult result) {
        return createOne(dto, result, request, response, UserConverters.createAccount(),
                (rest, tDto) -> {
                    tDto.encodedPassword(passwordEncoder.encode(tDto.getPassword()));
                }, (rest, t, tDto) -> {
                    restControllerService.getApplicationEventPublisher().publishEvent(new RegistrationCompleteEvent(t));
                });
    }


    @PutMapping(ApiPaths.PATH_ACCOUNT_EMAIL)
    public ResponseEntity<?> accountEnableEmail(@PathVariable(Params.Path.EMAIL) String email) {
        service.accountEnableEmail(email);
        return restControllerService.getRestControllerResponseService().processSuccessOk();
    }


    @PutMapping(ApiPaths.PATH_ACCOUNT_ENABLE)
    public ResponseEntity<?> accountEnable(HttpServletRequest request, @PathVariable(Params.Path.TOKEN) String token) {
        Token.TokenStatus status = service.accountEnable(token);
        return restControllerService.getRestControllerResponseService().processTokenStatus(status, token);
    }

}
