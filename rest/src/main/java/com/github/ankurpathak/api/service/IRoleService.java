package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Role;

import java.util.Optional;

public interface IRoleService extends IDomainService<Role, String> {
    Optional<Role> findByName(String roleName);
}
