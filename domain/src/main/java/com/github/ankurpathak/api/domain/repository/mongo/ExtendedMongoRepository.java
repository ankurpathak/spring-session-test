package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.Domain;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface ExtendedMongoRepository<T extends Domain<ID>, ID extends Serializable> extends MongoRepository<T, ID> , ICustomizedDomainRepository<T, ID> {
}
