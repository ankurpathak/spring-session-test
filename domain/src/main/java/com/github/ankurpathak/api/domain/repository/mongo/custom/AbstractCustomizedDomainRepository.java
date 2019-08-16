package com.github.ankurpathak.api.domain.repository.mongo.custom;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.mongo.DomainSingleFieldSearchTypedAggregation;
import com.github.ankurpathak.api.domain.mongo.util.CriteriaUtil;
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
import java.util.stream.Collectors;

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
    public Page<T> findAllPaginated(Pageable pageable, Class<T> type) {
        Criteria criteria = new Criteria();
        Criteria businessCriteria = CriteriaUtil.converToBusinessCriteria(type, criteria);
        return findByCriteriaPaginated(businessCriteria, pageable, type);
    }

    @Override
    public<S extends T> Page<S> findAllPaginated(Pageable pageable, Class<S> type, String view) {
        Criteria criteria = new Criteria();
        Criteria businessCriteria = CriteriaUtil.converToBusinessCriteria(type, criteria);
        return findByCriteriaPaginated(businessCriteria, pageable, type, view);
    }

    @Override
    public Page<T> findByCriteriaPaginated(Criteria criteria, Pageable pageable, Class<T> type) {
        List<T> list = findByCriteria(criteria, pageable, type);
        return new PageImpl<>(
                list,
                pageable,
                countByCriteria(criteria, type)
        );
    }

    @Override
    public <S extends T> Page<S> findByCriteriaPaginated(Criteria criteria, Pageable pageable, Class<S> type, String view) {
        List<S> list = findByCriteria(criteria, pageable, type, view);
        return new PageImpl<>(
                list,
                pageable,
                countByCriteria(criteria, type, view)
        );
    }


    @Override
    public <S extends T> List<S> findByCriteria(Criteria criteria, Pageable pageable, Class<S> type, String view) {
        return template.find(
                new Query().with(pageable).addCriteria(criteria),
                type,
                view

        );
    }

    @Override
    public List<T> findByCriteria(Criteria criteria, Pageable pageable, Class<T> type) {
        return template.find(
                new Query().with(pageable).addCriteria(criteria),
                type
        );
    }


    @Override
    public long countByCriteria(Criteria criteria, Class<T> type) {
        return template.count(new Query().addCriteria(criteria), type);
    }

    @Override
    public <S extends T> long countByCriteria(Criteria criteria, Class<S> type, String view) {
        return template.count(new Query().addCriteria(criteria), view);
    }


    @Override
    public BulkWriteResult bulkInsertMany(final Class<T> type, final List<T> domains) {
        BulkOperations ops = template.bulkOps(BulkOperations.BulkMode.ORDERED, type);
        ops  = ops.insert(domains);
        return ops.execute();
    }


}
