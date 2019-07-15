package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedUserRepository;

import java.math.BigInteger;

public interface IUserRepository extends ExtendedMongoRepository<User, BigInteger>, CustomizedUserRepository {

}
