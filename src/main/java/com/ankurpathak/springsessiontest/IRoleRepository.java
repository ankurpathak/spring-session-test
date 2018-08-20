package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IRoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);
}
