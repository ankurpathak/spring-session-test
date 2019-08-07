package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Role;
import com.github.ankurpathak.api.domain.repository.mongo.IRoleRepository;
import com.github.ankurpathak.api.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService extends AbstractDomainService<Role, String> implements IRoleService {

    private final IRoleRepository dao;

    public RoleService(IRoleRepository dao) {
        super(dao);
        this.dao = dao;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return dao.findByName(name);
    }
}
