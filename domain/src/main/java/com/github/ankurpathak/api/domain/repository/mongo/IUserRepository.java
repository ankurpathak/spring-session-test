package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedUserRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Optional;

public interface IUserRepository extends ExtendedMongoRepository<User, BigInteger>, CustomizedUserRepository {

    @Query("{ $or: [ { _id: ?0 }, { 'email.value' : ?1 }] }")
    Optional<User> byCandidateKey(@Param(Params.ID) BigInteger id, @Param("candidateKey") String candidateKey);

    @Query("{ 'email.value' : ?0 }")
    Optional<User> byEmail(@Param(Params.EMAIL) String email);

    @Query("{ 'email.tokenId' : ?0 }")
    Optional<User> byEmailTokenId(@Param(Params.TOKEN_ID) String tokenId);

    @Query("{ 'password.tokenId' : ?0 }")
    Optional<User> byPasswordTokenId(@Param(Params.TOKEN_ID) String tokenId);
}
