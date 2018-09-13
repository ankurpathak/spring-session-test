package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.repository.Query;

import java.math.BigInteger;
import java.util.Optional;

public interface IUserRepository extends ExtendedRepository<User, BigInteger>, CustomizedUserRepository {

    @Query("{ $or: [ { id: ?0 }, { \"email.value\" : ?1 }] }")
    Optional<User> findByCandidateKey(BigInteger id, String candidateKey);
}
