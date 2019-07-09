package com.github.ankurpathak.api.domain.repository.mongo.custom.impl;

import com.github.ankurpathak.api.domain.model.Sequence;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.domain.repository.mongo.ISequenceRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.AbstractCustomizedDomainRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedUserRepository;
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
