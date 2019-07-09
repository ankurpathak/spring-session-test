package com.github.ankurpathak.api.domain.repository.mongo.custom;

import com.github.ankurpathak.api.domain.model.Sequence;
import com.github.ankurpathak.api.domain.repository.mongo.ICustomizedDomainRepository;

import java.math.BigInteger;

public interface CustomizedSequenceRepository extends ICustomizedDomainRepository<Sequence, String> {
    BigInteger next(String id);
}
