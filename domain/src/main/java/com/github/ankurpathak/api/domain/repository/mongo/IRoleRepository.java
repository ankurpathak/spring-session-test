package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.Role;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedRoleRepository;

import java.util.Optional;

public interface IRoleRepository extends ExtendedMongoRepository<Role, String>, CustomizedRoleRepository {
    Optional<Role> findByName(String name);
}
