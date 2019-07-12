package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Role;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.exception.InvalidException;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.security.dto.CustomUserDetails;
import com.github.ankurpathak.api.service.impl.UserService;
import com.github.ankurpathak.api.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.valid4j.Assertive.require;

@Service
public class AccountService implements IAccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);


    private final IUserService userService;
    private final ITokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;

    protected AccountService(IUserService userService, ITokenService tokenService, PasswordEncoder passwordEncoder, IEmailService emailService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
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
                                        Optional.ofNullable(x.getTokenId())
                                                .filter(String::isEmpty)
                                                .ifPresentOrElse(tokenService::deleteById,() -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.EMAIL_TOKEN_ID, String.valueOf(user.getId())));
                                        tokenService.generateToken().ifPresentOrElse(token -> {
                                            System.out.println(token);
                                            userService.saveEmailToken(user, token);
                                            emailService.sendForAccountEnable(user, token, async);
                                        }, () -> LogUtil.logNull(log, Token.class.getSimpleName()));

                                    }, () -> LogUtil.logFieldNull(log, User.class.getSimpleName(), Model.User.Field.EMAIL, String.valueOf(user.getId())));

                        }, () -> { throw new NotFoundException(email, Params.EMAIL, User.class.getSimpleName(), ApiCode.NOT_FOUND);}
                );
    }

    @Override
    public Token.TokenStatus accountEnable(@Nonnull String token) {
        require(token, not(emptyString()));
        return verifyEmailToken(token);
    }


    private Token.TokenStatus verifyEmailToken(String value) {
        var token = tokenService.byValue(value);
        if (token.isPresent()) {
            final Calendar cal = Calendar.getInstance();
            if (token.get().getExpiry().isBefore(Instant.now())) {
                tokenService.delete(token.get());
                return Token.TokenStatus.EXPIRED;
            }
            Optional<User> user = userService.byEmailTokenId(token.get().getId());
            if (user.isPresent()) {
                user.get().setEnabled(true);
                user.get().getEmail().setTokenId(null);
                user.get().getEmail().setChecked(true);
                userService.update(user.get());
                tokenService.delete(token.get());
                return Token.TokenStatus.VALID;
            }

            return Token.TokenStatus.INVALID;

        } else return Token.TokenStatus.EXPIRED;

    }




}
