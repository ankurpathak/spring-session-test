package com.ankurpathak.springsessiontest;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService extends AbstractDomainService<User,BigInteger> implements IUserService  {

    private final IUserRepository dao;

    public UserService(IUserRepository dao) {
        this.dao = dao;
    }

    @Override
    protected ExtendedRepository<User,BigInteger> getDao() {
        return dao;
    }

    @Override
    public Optional<User> findByCandidateKey(String candidateKey) {
        return dao.findByCandidateKey(PrimitiveUtils.toBigInteger(candidateKey), candidateKey);
    }

    @Override
    public void createContactVerificationToken(User user, Contact email) {
        Token token = new Token(UUID.randomUUID().toString());
        email.setToken(token);
        update(user);
    }
}
