package com.ankurpathak.springsessiontest;

import java.math.BigInteger;

public interface CustomizedUserRepository extends ICustomizedDomainRepository<User, BigInteger> {
    User persist(User user);
}
