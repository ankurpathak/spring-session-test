package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Role;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.exception.InvalidException;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.security.dto.CustomUserDetails;
import com.github.ankurpathak.api.service.IEmailService;
import com.github.ankurpathak.api.service.IPasswordService;
import com.github.ankurpathak.api.service.ITokenService;
import com.github.ankurpathak.api.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.valid4j.Assertive.require;

@Service
public class PasswordService  implements IPasswordService {

    private final IUserService userService;
    private final ITokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;

    protected PasswordService(IUserService userService, ITokenService tokenService, PasswordEncoder passwordEncoder, IEmailService emailService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }


    @Override
    public Token.TokenStatus forgetPasswordEnable(String token) {
        require(token, not(emptyString()));
        return verifyPasswordToken(token);
    }


    @Override
    public void validateExistingPassword(User user, UserDto dto) {
        require(user, notNullValue());
        require(dto, notNullValue());
        Optional.ofNullable(user.getPassword())
                .ifPresentOrElse(password -> {
                    if (!passwordEncoder.matches(dto.getCurrentPassword(), password.getValue()))
                        throw new InvalidException(ApiCode.INVALID_PASSWORD, Params.PASSWORD, dto.getCurrentPassword());

                }, () -> {
                    throw new InvalidException(ApiCode.INVALID_PASSWORD, Params.PASSWORD, dto.getCurrentPassword());
                });

    }


    private Token.TokenStatus verifyPasswordToken(String value) {
        var token = tokenService.byValue(value);
        if (token.isPresent()) {
            if (token.get().getExpiry().isBefore(Instant.now())) {
                tokenService.delete(token.get());
                return Token.TokenStatus.EXPIRED;
            }
            Optional<User> user = userService.byPasswordTokenId(token.get().getId());
            if (user.isPresent()) {
                UserDetails details = CustomUserDetails.getInstance(user.get(), Set.of(Role.Privilege.PRIV_FORGET_PASSWORD));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        details,
                        null,
                        details.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                tokenService.delete(token.get());
                return Token.TokenStatus.VALID;
            }

            return Token.TokenStatus.INVALID;
        } else return Token.TokenStatus.EXPIRED;

    }

    @Override
    public void forgotPasswordEmail(String email, boolean async) {
        userService.byEmail(email).ifPresentOrElse(user -> {
            if (user.getPassword() != null && !StringUtils.isEmpty(user.getPassword().getTokenId()))
                tokenService.deleteById(user.getPassword().getTokenId());
            tokenService.generateToken()
                    .ifPresent(token -> {
                        userService.savePasswordToken(user, token);
                        emailService.sendForForgetPassword(user, token, async);
                    });
        }, ()-> {
            throw new NotFoundException(email, Params.EMAIL, User.class.getSimpleName(), ApiCode.NOT_FOUND);
        });



    }
}
