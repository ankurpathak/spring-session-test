package com.github.ankurpathak.app.domain.repository.mongo;

import com.github.ankurpathak.app.domain.model.Role;
import com.github.ankurpathak.app.domain.repository.mongo.custom.CustomizedRoleRepository;

import java.util.Optional;

public interface IRoleRepository extends ExtendedMongoRepository<Role, String>, CustomizedRoleRepository {
    Optional<Role> findByName(String name);
}
