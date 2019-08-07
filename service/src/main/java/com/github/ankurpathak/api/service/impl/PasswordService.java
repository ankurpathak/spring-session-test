package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.exception.InvalidException;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.IEmailService;
import com.github.ankurpathak.api.service.IPasswordService;
import com.github.ankurpathak.api.service.ITokenService;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.valid4j.Assertive.require;

@Service
public class PasswordService  implements IPasswordService {

    private static final Logger log = LoggerFactory.getLogger(PasswordService.class);


    private final CustomUserDetailsService customUserDetailsService;
    private final ITokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;

    protected PasswordService(CustomUserDetailsService customUserDetailsService, ITokenService tokenService, PasswordEncoder passwordEncoder, IEmailService emailService) {
        this.customUserDetailsService = customUserDetailsService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }


    @Override
    public Token.TokenStatus forgetPasswordEnable(String tokenOtp) {
        require(tokenOtp, not(emptyString()));
        return tokenService.checkForgetPasswordTokenStatus(tokenOtp);
    }


    @Override
    public void validateExistingPassword(User user, UserDto dto) {
        require(user, notNullValue());
        require(dto, notNullValue());
        Optional.ofNullable(user.getPassword())
                .ifPresentOrElse(password -> {
                    if (!passwordEncoder.matches(dto.getCurrentPassword(), password))
                        throw new InvalidException(ApiCode.INVALID_PASSWORD, Params.PASSWORD, dto.getCurrentPassword());

                }, () -> {
                    throw new InvalidException(ApiCode.INVALID_PASSWORD, Params.PASSWORD, dto.getCurrentPassword());
                });

    }




    @Override
    public void forgotPasswordEmail(String email, boolean async) {
        customUserDetailsService.getUserService().byEmail(email).ifPresentOrElse(user -> {
            tokenService.generateForgetPasswordToken(email)
                    .ifPresentOrElse(token -> {
                        tokenService.findForgetPasswordToken(token.getValue())
                                .ifPresentOrElse(reverseToken-> {
                                    emailService.sendForForgetPassword(user, reverseToken);
                                }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));
                    }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));
        }, ()-> {
            throw new NotFoundException(email, Params.EMAIL, User.class.getSimpleName(), ApiCode.NOT_FOUND);
        });
    }

}
