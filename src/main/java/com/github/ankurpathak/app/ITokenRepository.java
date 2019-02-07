package com.github.ankurpathak.app;

import com.github.ankurpathak.app.domain.repository.custom.CustomizedTokenRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ITokenRepository extends ExtendedMongoRepository<Token, String>, CustomizedTokenRepository {
    @Query("{ 'value' : ?0 }")
    Optional<Token> byValue(@Param(Params.VALUE) String value);
}
