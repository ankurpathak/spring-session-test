package com.github.ankurpathak.app.service;

import com.github.ankurpathak.app.Token;
import com.github.ankurpathak.app.User;
import com.github.ankurpathak.app.UserDto;
import com.github.ankurpathak.app.service.IDomainService;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface IUserService  extends IDomainService<User, BigInteger> {

    Optional<User> byCandidateKey(String s);
    void saveEmailToken(User user, Token token);
    Optional<User> byEmail(String email);
    Optional<User> byEmailTokenId(String tokenId);


    void accountEnableEmail(String email);

    Token.TokenStatus accountEnable(String token);

    Token.TokenStatus forgetPasswordEnable(String token);

    Optional<User> byPasswordTokenId(String tokenId);

    void forgotPasswordEmail(User user);

    void savePasswordToken(User user, Token token);

    void validateExistingPassword(User user, UserDto dto);

    Map<String, Object> possibleCandidateKeys(String username);

    Set<String> possibleContacts(String username);
}