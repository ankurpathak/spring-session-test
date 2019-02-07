package com.github.ankurpathak.app;

import com.github.ankurpathak.app.domain.repository.custom.CustomizedRoleRepository;

import java.util.Optional;

public interface IRoleRepository extends ExtendedMongoRepository<Role, String>, CustomizedRoleRepository {
    Optional<Role> findByName(String name);
}
