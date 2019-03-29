package com.github.ankurpathak.app.domain.repository.mongo.custom;

import com.github.ankurpathak.app.domain.repository.mongo.ICustomizedDomainRepository;
import com.github.ankurpathak.app.SingleFieldSearchAggregation;
import com.github.ankurpathak.app.domain.model.Domain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

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
    public Page<T> findByCriteriaPaginated(Criteria criteria, Pageable pageable, Class<T> type) {
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

    @Override
    public Stream<T> findByCriteria(Criteria criteria, Pageable pageable, Class<T> type) {
        return template.find(
                new Query().with(pageable).addCriteria(criteria),
                type
        ).stream();
    }


    @Override
    public long countByCriteria(Criteria criteria, Class<T> type) {
        return template.count(new Query().addCriteria(criteria), type);
    }
}