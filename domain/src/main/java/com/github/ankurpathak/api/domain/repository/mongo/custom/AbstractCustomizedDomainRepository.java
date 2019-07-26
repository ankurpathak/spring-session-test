package com.github.ankurpathak.api.domain.repository.mongo.custom;

import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.mongo.DomainSingleFieldSearchTypedAggregation;
import com.github.ankurpathak.api.domain.repository.mongo.ICustomizedDomainRepository;
import com.mongodb.bulk.BulkWriteResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
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
        DomainSingleFieldSearchTypedAggregation<T, ID> aggregation = new DomainSingleFieldSearchTypedAggregation<>(template);
        return aggregation.getPage(field, value, pageable, type);
    }

    @Override
    public Page<String> listField(String field, String value, Pageable pageable, Class<T> type) {
        DomainSingleFieldSearchTypedAggregation<T, ID> aggregation = new DomainSingleFieldSearchTypedAggregation<>(template);
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


    @Override
    public BulkWriteResult bulkInsertMany(final Class<T> type, final List<T> domains) {
        BulkOperations ops = template.bulkOps(BulkOperations.BulkMode.ORDERED, type);
        ops.insert(domains);
        return ops.execute();
    }
}
