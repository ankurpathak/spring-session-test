package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Token;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedTokenRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ITokenRepository extends ExtendedMongoRepository<Token, String>, CustomizedTokenRepository {
    @Query("{ 'value' : ?0 }")
    Optional<Token> byValue(@Param(Params.VALUE) String value);
}
