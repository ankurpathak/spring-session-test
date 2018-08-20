package com.ankurpathak.springsessiontest;

import java.util.Optional;

public interface IRoleService extends IDomainService<Role, String> {
    Optional<Role> findByName(String roleName);
}
