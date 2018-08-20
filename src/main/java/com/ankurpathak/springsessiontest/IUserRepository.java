package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface IUserRepository extends MongoRepository<User, String> {

    @Query("{ $or: [ { id: ?0 }, { email: ?0 }] }")
    Optional<User> findByCandidateKey(String candidateKey);
}
