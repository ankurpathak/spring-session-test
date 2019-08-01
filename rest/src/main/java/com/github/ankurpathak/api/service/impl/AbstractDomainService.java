package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.domain.mongo.util.CriteriaUtil;
import com.github.ankurpathak.api.domain.repository.mongo.ExtendedMongoRepository;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.util.MatcherUtil;
import com.mongodb.bulk.BulkWriteResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.valid4j.Assertive;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.valid4j.Assertive.require;

public abstract class AbstractDomainService<T extends Domain<ID>, ID extends Serializable> implements IDomainService<T, ID> {

    private final ExtendedMongoRepository<T, ID> dao;

    protected AbstractDomainService(ExtendedMongoRepository<T, ID> dao) {
        this.dao = dao;
    }


    @Override
    public Optional<T> findById(final ID id) {
        require(id, notNullValue());
        return dao.findById(id);
    }

    @Override
    public List<T> findAll() {
        return dao.findAll();
    }

    @Override
    public Page<T> findByCriteriaPaginated(final Criteria criteria, final Pageable pageable, final Class<T> type) {
        require(criteria, notNullValue());
        require(pageable, notNullValue());
        require(type, notNullValue());
        Criteria businessCriteria = CriteriaUtil.converToBusinessCriteria(type, criteria);
        return dao.findByCriteriaPaginated(businessCriteria, pageable, type);
    }


    @Override
    public List<T> findByCriteria(final Criteria criteria, final Pageable pageable, final Class<T> type) {
        require(criteria, notNullValue());
        require(pageable, notNullValue());
        require(type, notNullValue());
        Criteria businessCriteria = CriteriaUtil.converToBusinessCriteria(type, criteria);
        return dao.findByCriteria(businessCriteria, pageable, type);
    }



    @Override
    public long countByCriteria(final Criteria criteria, final Pageable pageable, final Class<T> type) {
        require(criteria, notNullValue());
        require(type, notNullValue());
        Criteria businessCriteria = CriteriaUtil.converToBusinessCriteria(type, criteria);
        return dao.countByCriteria(businessCriteria, type);
    }


    @Override
    public Page<T> findAllPaginated(final Pageable pageable, Class<T> type) {
        require(pageable, notNullValue());
        return dao.findAllPaginated(pageable, type);
    }



    @Override
    public T create(T entity) {
        require(entity, notNullValue());
        return dao.insert(entity);
    }

    @Override
    public T update(T entity) {
        require(entity, notNullValue());
        return dao.save(entity);
    }

    @Override
    public Iterable<T> createAll(Iterable<T> entities) {
        Assertive.require(entities, MatcherUtil.notIterableEmpty());
        return dao.insert(entities);
    }

    @Override
    public void delete(final T entity) {
        require(entity, notNullValue());
        dao.delete(entity);
    }

    @Override
    public void deleteById(ID id) {
        require(id, notNullValue());
        dao.deleteById(id);
    }


    @Override
    public String domainName() {
        String name = this.getClass().getSimpleName();
        name = name != null ? name : "";
        int index = name.indexOf('S');
        index = index > -1 ? index : 1;
        return name.substring(1, index).trim();
    }


    @Override
    public Page<T> findByField(String field, String value, Pageable pageable, Class<T> type) {
        require(field, not(emptyOrNullString()));
        require(value, not(emptyOrNullString()));
        require(pageable, notNullValue());
        require(type, notNullValue());
        return dao.findByField(field, value, pageable, type);
    }

    @Override
    public Page<String> listField(@Nonnull String field, @Nonnull String value, @Nonnull Pageable pageable, Class<T> type) {
        require(field, not(emptyOrNullString()));
        require(value, not(emptyOrNullString()));
        require(pageable, notNullValue());
        require(type, notNullValue());
        return dao.listField(field, value, pageable, type);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<T> domains) {
        require(domains, MatcherUtil.notIterableEmpty());
        dao.deleteAll(domains);
    }

    @Override
    public BulkWriteResult bulkInsertMany(Class<T> type, List<T> domains) {
        require(type, notNullValue());
        require(domains, MatcherUtil.notCollectionEmpty());
        return dao.bulkInsertMany(type, domains);
    }
    @Override
    public <S extends T> Page<S> findAllPaginated(Pageable pageable, Class<S> type, String view){
        require(pageable, notNullValue());
        require(type, notNullValue());
        require(view, MatcherUtil.notStringEmpty());
        return dao.findAllPaginated(pageable, type, view);
    }
    @Override
    public long countByCriteria(final Criteria criteria, final Class<T> type, final String view){
        require(criteria, notNullValue());
        require(type, notNullValue());
        require(view, notNullValue());
        Criteria businessCriteria = CriteriaUtil.converToBusinessCriteria(type, criteria);
        return dao.countByCriteria(businessCriteria, type, view);
    }
    @Override
    public List<T> findByCriteria(final Criteria criteria, final Pageable pageable, final Class<T> type, final String view){
        require(criteria, notNullValue());
        require(type, notNullValue());
        require(view, notNullValue());
        Criteria businessCriteria = CriteriaUtil.converToBusinessCriteria(type, criteria);
        return dao.findByCriteria(businessCriteria, pageable, type, view);
    }
    @Override
    public Page<T> findByCriteriaPaginated(final Criteria criteria, final Pageable pageable, final Class<T> type, final String view){
        require(criteria, notNullValue());
        require(type, notNullValue());
        require(view, notNullValue());
        Criteria businessCriteria = CriteriaUtil.converToBusinessCriteria(type, criteria);
        return dao.findByCriteriaPaginated(businessCriteria, pageable, type, view);
    }
}
