package com.github.ankurpathak.api.service.impl;


import com.github.ankurpathak.api.domain.model.Role;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.mongo.ITokenRepository;
import com.github.ankurpathak.api.security.dto.CustomUserDetails;
import com.github.ankurpathak.api.security.service.CustomUserDetailsService;
import com.github.ankurpathak.api.service.ITokenService;
import com.github.ankurpathak.api.util.MatcherUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.valid4j.Assertive.require;

@Service
public class TokenService extends AbstractDomainService<Token, String> implements ITokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);


    private final ITokenRepository dao;
    private final CustomUserDetailsService customUserDetailsService;

    public TokenService(ITokenRepository dao, CustomUserDetailsService customUserDetailsService) {
        super(dao);
        this.dao = dao;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Optional<Token> generateAccountToken(String email) {
        require(email, MatcherUtil.notStringEmpty());
        int i = 1;
        do {
            String tokenOtp = RandomStringUtils.randomAlphanumeric(8, 8);
            Token token = Token.getInstance()
                    .id(String.format("users:account:%s", tokenOtp))
                    .value(email);
            try{
                create(token);
                Token reverseToken = Token.getInstance()
                        .id(String.format("users:account:%s", email))
                        .value(tokenOtp)
                        .expiry(token.getExpiry());
                try{
                    create(reverseToken);
                }catch (DuplicateKeyException dEx){
                    deleteById(reverseToken.getId());
                    create(reverseToken);
                }
                return Optional.of(token);
            }catch (DuplicateKeyException dEx){
                log.info("Account token generation failed: {}", i);
                i++;
            }
        }while (i < 10);
        return Optional.empty();
    }





    public Optional<Token> tryToSaveGeneratedToken(Token token){
        try{
            create(token);
        }catch (DuplicateKeyException dEx){
            deleteById(token.getId());
            create(token);
        }
        return Optional.of(token);
    }

    @Override
    public Optional<Token> generateForgetPasswordToken(String email) {
        require(email, MatcherUtil.notStringEmpty());
        int i = 1;
        do {
            String tokenOtp = RandomStringUtils.randomAlphanumeric(8, 8);
            Token token = Token.getInstance()
                    .id(String.format("users:forget-password:%s", tokenOtp))
                    .value(email);
            try{
                create(token);
                Token reverseToken = Token.getInstance()
                        .id(String.format("users:forget-password:%s", email))
                        .value(tokenOtp)
                        .expiry(token.getExpiry());
                try{
                    create(reverseToken);
                }catch (DuplicateKeyException dEx){
                    deleteById(reverseToken.getId());
                    create(reverseToken);
                }
                return Optional.of(token);
            }catch (DuplicateKeyException dEx){
                log.info("Forget password token generation failed: {}", i);
                i++;
            }
        }while (i < 10);
        return Optional.empty();
    }

    @Override
    public Optional<Token> generatePhoneToken(String phone) {
        require(phone, MatcherUtil.notStringEmpty());
        Token token = Token.getInstance()
                .id(String.format("users:phone:%s", phone))
                .value(RandomStringUtils.randomAlphanumeric(8, 8));
        return tryToSaveGeneratedToken(token);
    }

    @Override
    public Token.TokenStatus checkAccountTokenStatus(String tokenOtp) {
        require(tokenOtp, MatcherUtil.notStringEmpty());
        Optional<Token> token = findAccountToken(tokenOtp);
        if (token.isPresent()) {
            if (token.get().getExpiry().isBefore(Instant.now())) {
                deleteById(token.get().getId());
                deleteAccountToken(token.get().getValue());
                return Token.TokenStatus.EXPIRED;
            }
            Optional<User> user = customUserDetailsService.getUserService().byEmail(token.get().getValue());
            if (user.isPresent()) {
                if(!user.get().isEnabled())
                    user.get().setEnabled(true);
                user.get().getEmail().setChecked(true);
                customUserDetailsService.getUserService().update(user.get());
                deleteById(token.get().getId());
                deleteAccountToken(token.get().getValue());
                return Token.TokenStatus.VALID;
            }
            return Token.TokenStatus.INVALID;
        } else return Token.TokenStatus.EXPIRED;
    }

    @Override
    public Token.TokenStatus checkForgetPasswordTokenStatus(String tokenOtp) {
        require(tokenOtp, MatcherUtil.notStringEmpty());
        Optional<Token> token = findForgetPasswordToken(tokenOtp);
        if (token.isPresent()) {
            if (token.get().getExpiry().isBefore(Instant.now())) {
                deleteById(token.get().getId());
                deleteForgetPasswordToken(token.get().getValue());
                return Token.TokenStatus.EXPIRED;
            }
            Optional<User> user = customUserDetailsService.getUserService().byEmail(token.get().getValue());
            if (user.isPresent()) {
                UserDetails details = CustomUserDetails.getInstance(user.get(), Set.of(Role.Privilege.PRIV_FORGET_PASSWORD));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        details,
                        null,
                        details.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                deleteById(token.get().getId());
                deleteForgetPasswordToken(token.get().getValue());
                return Token.TokenStatus.VALID;
            }

            return Token.TokenStatus.INVALID;
        } else return Token.TokenStatus.EXPIRED;
    }


    @Override
    public Token.TokenStatus checkPhoneTokenStatus(String phone, String tokenOtp) {
        require(phone, MatcherUtil.notStringEmpty());
        require(tokenOtp, MatcherUtil.notStringEmpty());
        Optional<Token> token = findPhoneToken(phone);
        if (token.isPresent()) {
            if (token.get().getExpiry().isBefore(Instant.now())) {
                deleteById(token.get().getId());
                return Token.TokenStatus.EXPIRED;
            }
            if(Objects.equals(tokenOtp, token.get().getValue())){
                Optional<User> user = customUserDetailsService.getUserService().byPhone(phone);
                if (user.isPresent()) {
                    if(!user.get().isEnabled())
                        user.get().setEnabled(true);
                    user.get().getPhone().setChecked(true);
                    customUserDetailsService.getUserService().update(user.get());
                    deleteById(token.get().getId());
                    return Token.TokenStatus.VALID;
                }
            }
            return Token.TokenStatus.INVALID;
        } else return Token.TokenStatus.EXPIRED;
    }

    @Override
    public Optional<Token> findForgetPasswordToken(String key){
        return findById(String.format("users:forget-password:%s", key));
    }

    @Override
    public void deleteForgetPasswordToken(String key){
        deleteById(String.format("users:forget-password:%s", key));
    }

    @Override
    public void deleteAccountToken(String key){
        deleteById(String.format("users:account:%s", key));
    }


    @Override
    public Optional<Token> findAccountToken(String key){
        return findById(String.format("users:account:%s", key));
    }



    @Override
    public Optional<Token> findPhoneToken(String phone){
        return findById(String.format("users:phone:%s", phone));
    }








}
