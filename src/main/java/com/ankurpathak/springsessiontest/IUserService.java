package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface IUserService  extends IDomainService<User, String> {

    @Query
    Optional<User> findByCandidateKey(String s);
}
