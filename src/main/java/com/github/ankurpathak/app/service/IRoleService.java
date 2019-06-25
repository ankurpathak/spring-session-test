package com.github.ankurpathak.app.service;

import com.github.ankurpathak.app.domain.model.Role;

import java.util.Optional;

public interface IRoleService extends IDomainService<Role, String> {
    Optional<Role> findByName(String roleName);
}
