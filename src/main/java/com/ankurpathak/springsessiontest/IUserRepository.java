package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Optional;

import static com.ankurpathak.springsessiontest.Params.*;

public interface IUserRepository extends ExtendedRepository<User, BigInteger>, CustomizedUserRepository {

    @Query("{ $or: [ { _id: ?0 }, { 'email.value' : ?1 }] }")
    Optional<User> byCandidateKey(@Param(ID) BigInteger id, @Param("candidateKey") String candidateKey);

    @Query("{ 'email.value' : ?0 }")
    Optional<User> byEmail(@Param(EMAIL) String email);

    @Query("{ 'email.tokenId' : ?0 }")
    Optional<User> byEmailTokenId(@Param(TOKEN_ID) String tokenId);

    @Query("{ 'password.tokenId' : ?0 }")
    Optional<User> byPasswordTokenId(@Param(TOKEN_ID) String tokenId);
}
