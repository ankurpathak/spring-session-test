package com.github.ankurpathak.api.domain.repository.mongo.custom.impl;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Sequence;
import com.github.ankurpathak.api.domain.repository.mongo.ISequenceRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.AbstractCustomizedDomainRepository;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedBusinessRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class CustomizedBusinessRepositoryImpl extends AbstractCustomizedDomainRepository<Business, BigInteger> implements CustomizedBusinessRepository {
    private final ISequenceRepository sequenceRepository;
    public CustomizedBusinessRepositoryImpl(MongoTemplate template, ISequenceRepository sequenceRepository) {
        super(template);
        this.sequenceRepository = sequenceRepository;
    }



    @Override
    public Business persist(final Business business) {
        template.insert(business.id(sequenceRepository.next(Sequence.ID_BUSINESS_SEQ)));
        return business;
    }
}
