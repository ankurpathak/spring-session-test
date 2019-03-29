package com.github.ankurpathak.app.domain.repository.mongo.custom;

import com.github.ankurpathak.app.domain.repository.mongo.ICustomizedDomainRepository;
import com.github.ankurpathak.app.User;

import java.math.BigInteger;

public interface CustomizedUserRepository extends ICustomizedDomainRepository<User, BigInteger> {
    User persist(User user);
}
