package com.ankurpathak.springsessiontest;

import java.math.BigInteger;
import java.util.Optional;

public interface IUserService  extends IDomainService<User, BigInteger> {

    Optional<User> findByCandidateKey(String s);

    void createContactVerificationToken(User user, Contact email);
}
