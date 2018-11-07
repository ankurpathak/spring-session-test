package com.ankurpathak.springsessiontest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractCustomizedDomainRepository<T extends Domain<ID>, ID extends Serializable> implements ICustomizedDomainRepository<T, ID> {

    protected final MongoTemplate template;

    protected AbstractCustomizedDomainRepository(MongoTemplate template) {
        this.template = template;
    }


    @Override
    public Page<T> findByField(String field, String value, Pageable pageable, Class<T> type) {
        SingleFieldSearchAggregation<T, ID> aggregation = new SingleFieldSearchAggregation<>(template);
        return aggregation.getPage(field, value, pageable, type);
    }

    @Override
    public Page<String> listField(String field, String value, Pageable pageable, Class<T> type) {
        SingleFieldSearchAggregation<T, ID> aggregation = new SingleFieldSearchAggregation<>(template);
        return aggregation.getFieldPage(field, value, pageable, type);
    }

    @Override
    public Page<T> findByCriteria(Criteria criteria, Pageable pageable, Class<T> type) {
        List<T> list = template.find(
                new Query().with(pageable).addCriteria(criteria),
                type
        );
        return new PageImpl<>(
                list,
                pageable,
                template.count(new Query().addCriteria(criteria), type)
        );
    }
}
