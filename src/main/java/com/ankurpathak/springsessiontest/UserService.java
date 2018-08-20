package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService extends AbstractDomainService<User, String> implements IUserService  {

    private final IUserRepository dao;

    public UserService(IUserRepository dao) {
        this.dao = dao;
    }

    @Override
    protected MongoRepository<User, String> getDao() {
        return dao;
    }

    @Override
    public Optional<User> findByCandidateKey(String candidateKey) {
        return dao.findByCandidateKey(candidateKey);
    }
}
