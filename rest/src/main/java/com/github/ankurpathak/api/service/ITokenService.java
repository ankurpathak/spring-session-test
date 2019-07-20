package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;

import java.math.BigInteger;
import java.util.Optional;

public interface ITokenService extends IDomainService<Token, String> {
    Optional<Token> generateAccountToken(String email);


    Optional<Token> generateForgetPasswordToken(String email);
    Optional<Token> generatePhoneToken(String phone);

    Token.TokenStatus checkAccountTokenStatus(String id);

    Token.TokenStatus checkForgetPasswordTokenStatus(String tokenOtp);

    Token.TokenStatus checkPhoneTokenStatus(String phone, String tokenOtp);

    Optional<Token> findForgetPasswordToken(String key);

    void deleteForgetPasswordToken(String key);

    void deleteAccountToken(String key);

    Optional<Token> findAccountToken(String key);



    Optional<Token> findPhoneToken(String phone);


    @FunctionalInterface
    interface ISendUserToken {
        void sendToken(User user, Token token);
    }


}
