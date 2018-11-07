package com.ankurpathak.springsessiontest;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class CustomizedUserRepositoryImpl extends AbstractCustomizedDomainRepository<User, BigInteger> implements CustomizedUserRepository {
    private final ISequenceRepository sequenceRepository;
    public CustomizedUserRepositoryImpl(MongoTemplate template, ISequenceRepository sequenceRepository) {
        super(template);
        this.sequenceRepository = sequenceRepository;
    }



    @Override
    public User persist(final User user) {
        template.insert(user.id(sequenceRepository.next(Sequence.ID_USER_SEQ)));
        return user;
    }
}
