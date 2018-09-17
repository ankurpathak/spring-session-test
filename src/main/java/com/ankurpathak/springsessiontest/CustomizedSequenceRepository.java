package com.ankurpathak.springsessiontest;

import java.math.BigInteger;

public interface CustomizedSequenceRepository extends IDomainCustomizedRepository<Sequence, String> {
    BigInteger next(String id);
}
