package com.github.ankurpathak.app.domain.repository.custom;

import com.github.ankurpathak.app.ICustomizedDomainRepository;
import com.github.ankurpathak.app.User;

import java.math.BigInteger;

public interface CustomizedUserRepository extends ICustomizedDomainRepository<User, BigInteger> {
    User persist(User user);
}
