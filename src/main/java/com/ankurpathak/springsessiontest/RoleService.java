package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService extends AbstractDomainService<Role, String> implements IRoleService{

    private final IRoleRepository dao;

    @Override
    protected MongoRepository<Role, String> getDao() {
        return dao;
    }


    public RoleService(IRoleRepository dao) {
        this.dao = dao;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return dao.findByName(name);
    }
}
