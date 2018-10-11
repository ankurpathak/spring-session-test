package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.ankurpathak.springsessiontest.Params.VALUE;

public interface ITokenRepository extends ExtendedMongoRepository<Token, String>, CustomizedTokenRepository {
    @Query("{ 'value' : ?0 }")
    Optional<Token> byValue(@Param(VALUE) String value);
}
