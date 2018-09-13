package com.ankurpathak.springsessiontest;

import java.util.Optional;

public interface IRoleRepository extends ExtendedRepository<Role, String>, CustomizedRoleRepository {
    Optional<Role> findByName(String name);
}
