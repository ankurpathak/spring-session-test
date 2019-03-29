package com.github.ankurpathak.app.service;

import com.github.ankurpathak.app.Role;
import com.github.ankurpathak.app.service.IDomainService;

import java.util.Optional;

public interface IRoleService extends IDomainService<Role, String> {
    Optional<Role> findByName(String roleName);
}
