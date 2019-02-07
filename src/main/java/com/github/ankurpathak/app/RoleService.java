package com.github.ankurpathak.app;

import com.github.ankurpathak.app.service.impl.AbstractDomainService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService extends AbstractDomainService<Role, String> implements IRoleService{

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
