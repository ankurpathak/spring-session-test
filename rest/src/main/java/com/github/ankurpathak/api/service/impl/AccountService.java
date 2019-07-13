package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.service.IAccountService;
import com.github.ankurpathak.api.service.IEmailService;
import com.github.ankurpathak.api.service.ITokenService;
import com.github.ankurpathak.api.service.IUserService;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Calendar;
import java.util.Optional;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.valid4j.Assertive.require;

@Service
public class AccountService implements IAccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);


    private final IUserService userService;
    private final ITokenService tokenService;
    private final IEmailService emailService;

    protected AccountService(IUserService userService, ITokenService tokenService, IEmailService emailService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }


    @Override
    public void accountEnableEmail(@Nonnull String email, boolean async) {
        require(email, notNullValue());
        userService.byEmail(email)
                .filter(user -> !user.isEnabled())
                .ifPresentOrElse(user -> {
                            Optional.ofNullable(user.getEmail())
                                    .ifPresentOrElse(x -> {
                                        tokenService.generateAccountToken(email)
                                                .ifPresentOrElse(token -> {
                                                    System.out.println(token);
                                                    emailService.sendForAccountEnable(user, token, async);
                                                }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));

                                    }, () -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.EMAIL, String.valueOf(user.getId())));

                        }, () -> {
                            throw new NotFoundException(email, Params.EMAIL, User.class.getSimpleName(), ApiCode.NOT_FOUND);
                        }
                );
    }

    @Override
    public Token.TokenStatus accountEnable(@Nonnull String tokenOtp) {
        require(tokenOtp, not(emptyString()));
        return tokenService.checkAccountTokenStatus(tokenOtp);
    }


}
