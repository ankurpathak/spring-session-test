package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.event.EmailTokenEvent;
import com.github.ankurpathak.api.event.LoginTokenEvent;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.service.IAccountService;
import com.github.ankurpathak.api.service.IEmailService;
import com.github.ankurpathak.api.service.ITokenService;
import com.github.ankurpathak.api.service.IUserService;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
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
    private final ApplicationEventPublisher applicationEventPublisher;

    protected AccountService(IUserService userService, ITokenService tokenService, IEmailService emailService, ApplicationEventPublisher applicationEventPublisher) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Override
    public void accountEnableEmail(@Nonnull String email) {
        require(email, notNullValue());
        userService.byEmail(email)
                .filter(user -> !user.isEnabled())
                .ifPresentOrElse(user -> {
                    applicationEventPublisher.publishEvent(new EmailTokenEvent(user));
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
