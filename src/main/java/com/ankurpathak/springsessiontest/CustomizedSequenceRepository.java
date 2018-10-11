package com.ankurpathak.springsessiontest;

import java.math.BigInteger;

public interface CustomizedSequenceRepository extends ICustomizedDomainRepository<Sequence, String> {
    BigInteger next(String id);
}
