package com.github.ankurpathak.app.domain.repository.custom;

import com.github.ankurpathak.app.ICustomizedDomainRepository;
import com.github.ankurpathak.app.Sequence;

import java.math.BigInteger;

public interface CustomizedSequenceRepository extends ICustomizedDomainRepository<Sequence, String> {
    BigInteger next(String id);
}
