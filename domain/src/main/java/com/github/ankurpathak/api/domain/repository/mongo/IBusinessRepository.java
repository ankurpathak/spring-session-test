package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.repository.mongo.custom.CustomizedBusinessRepository;

import java.math.BigInteger;

public interface IBusinessRepository extends ExtendedMongoRepository<Business, BigInteger>, CustomizedBusinessRepository {
}
