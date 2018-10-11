package com.ankurpathak.springsessiontest;

import java.util.Optional;

public interface IRoleRepository extends ExtendedMongoRepository<Role, String>, CustomizedRoleRepository {
    Optional<Role> findByName(String name);
}
