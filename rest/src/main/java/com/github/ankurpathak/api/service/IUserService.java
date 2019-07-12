package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.rest.controllor.dto.UserDto;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.model.User;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface IUserService  extends IDomainService<User, BigInteger> {
    Optional<User> byCandidateKey(String candidateValue);
    void saveEmailToken(User user, Token token);
    Optional<User> byEmail(String email);
    Optional<User> byEmailTokenId(String tokenId);
    Optional<User> byPasswordTokenId(String tokenId);
    void savePasswordToken(User user, Token token);
    Map<String, Object> possibleCandidateKeys(String username);
    Set<String> possibleContacts(String username);
}
