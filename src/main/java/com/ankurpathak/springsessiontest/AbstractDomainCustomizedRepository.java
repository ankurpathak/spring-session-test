package com.ankurpathak.springsessiontest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.Serializable;

public abstract class AbstractDomainCustomizedRepository <T extends Domain<ID>, ID extends Serializable> implements IDomainCustomizedRepository<T,ID> {


    abstract public MongoTemplate getTemplate();


    @Override
    public Page<T> search(String field, String value, Pageable pageable, Class<T> type) {
        SingleFieldSearchAggregation<T, ID> aggregation = new SingleFieldSearchAggregation<>(getTemplate());
        return aggregation.getPage(field, value, pageable, type);
    }

    @Override
    public Page<String> listField(String field, String value, Pageable pageable, Class<T> type) {
        SingleFieldSearchAggregation<T, ID> aggregation = new SingleFieldSearchAggregation<>(getTemplate());
        return aggregation.getFieldPage(field, value, pageable, type);
    }
}
