package com.github.ankurpathak.app.domain.repository.mongo.custom;

import com.github.ankurpathak.app.domain.repository.mongo.ICustomizedDomainRepository;
import com.github.ankurpathak.app.Sequence;

import java.math.BigInteger;

public interface CustomizedSequenceRepository extends ICustomizedDomainRepository<Sequence, String> {
    BigInteger next(String id);
}
