package com.github.ankurpathak.api.domain.repository.mongo.custom;

import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.mongo.ICustomizedDomainRepository;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

public interface CustomizedUserRepository extends ICustomizedDomainRepository<User, BigInteger> {
    User persist(User user);
    Collection<User> persistAll(Collection<User> users);
}
