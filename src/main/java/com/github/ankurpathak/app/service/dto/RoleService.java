package com.github.ankurpathak.app.service.dto;

import com.github.ankurpathak.app.domain.model.Role;
import com.github.ankurpathak.app.domain.repository.mongo.IRoleRepository;
import com.github.ankurpathak.app.service.IRoleService;
import com.github.ankurpathak.app.service.impl.AbstractDomainService;
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
