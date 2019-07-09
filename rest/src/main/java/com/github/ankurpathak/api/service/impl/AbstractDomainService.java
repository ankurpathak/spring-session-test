package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.repository.mongo.ExtendedMongoRepository;
import com.github.ankurpathak.api.service.IDomainService;
import com.github.ankurpathak.api.util.MatcherUtil;
import com.github.ankurpathak.api.domain.model.Domain;
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
        return dao.findByCriteriaPaginated(criteria, pageable, type);
    }

    @Override
    public Stream<T> findByCriteria(final Criteria criteria, final Pageable pageable, final Class<T> type) {
        require(criteria, notNullValue());
        require(pageable, notNullValue());
        require(type, notNullValue());
        return dao.findByCriteria(criteria, pageable, type);
    }

    @Override
    public long countByCriteria(final Criteria criteria, final Pageable pageable, final Class<T> type) {
        require(criteria, notNullValue());
        require(type, notNullValue());
        return dao.countByCriteria(criteria, type);
    }

        @Override
    public Page<T> findAllPaginated(final Pageable pageable) {
        require(pageable, notNullValue());
        return dao.findAll(pageable);
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
        Assertive.require(domains, MatcherUtil.notIterableEmpty());
        dao.deleteAll(domains);
    }


}
