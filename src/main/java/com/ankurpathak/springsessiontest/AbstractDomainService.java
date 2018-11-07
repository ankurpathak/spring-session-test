package com.ankurpathak.springsessiontest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.valid4j.Assertive.ensure;
import static com.ankurpathak.springsessiontest.MatcherUtil.*;

public abstract class AbstractDomainService<T extends Domain<ID>, ID extends Serializable> implements IDomainService<T, ID> {

    private final ExtendedMongoRepository<T, ID> dao;

    protected AbstractDomainService(ExtendedMongoRepository<T, ID> dao) {
        this.dao = dao;
    }

    @Override
    public Optional<T> findById(final ID id) {
        ensure(id, notNullValue());
        return dao.findById(id);
    }
    @Override
    public List<T> findAll() {
        return dao.findAll();
    }

    @Override
    public Page<T> findByCriteria(final Criteria criteria, final Pageable pageable, Class<T> type) {
        ensure(criteria, notNullValue());
        ensure(pageable, notNullValue());
        ensure(type, notNullValue());
        return dao.findByCriteria(criteria,pageable, type);
    }

    @Override
    public Page<T> findPaginated(final Pageable pageable) {
        ensure(pageable, notNullValue());
        return dao.findAll(pageable);
    }

    @Override
    public T create(T entity) {
        ensure(entity, notNullValue());
        return dao.insert(entity);
    }

    @Override
    public T update(T entity) {
        ensure(entity, notNullValue());
        return dao.save(entity);
    }

    @Override
    public Iterable<T> createAll(Iterable<T> entities) {
        ensure(entities, notIterableEmpty());
        return dao.insert(entities);
    }

    @Override
    public void delete(final T entity) {
        ensure(entity, notNullValue());
        dao.delete(entity);
    }

    @Override
    public void deleteById(ID id) {
        ensure(id, notNullValue());
        dao.deleteById(id);
    }


    @Override
    public String domainName(){
        String name = this.getClass().getSimpleName();
        name = name != null ? name : "";
        int index = name.indexOf('S');
        index = index > -1 ? index : 1;
        return name.substring(1, index).trim();
    }


    @Override
    public Page<T> findByField(String field, String value, Pageable pageable, Class<T> type) {
        ensure(field, not(emptyOrNullString()));
        ensure(value, not(emptyOrNullString()));
        ensure(pageable, notNullValue());
        ensure(type, notNullValue());
        return dao.findByField(field, value, pageable, type);
    }

    @Override
    public Page<String> listField(String field, String value, Pageable pageable, Class<T> type) {
        ensure(field, not(emptyOrNullString()));
        ensure(value, not(emptyOrNullString()));
        ensure(pageable, notNullValue());
        ensure(type, notNullValue());
        return dao.listField(field, value, pageable, type);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<T> domains) {
        ensure(domains, notIterableEmpty());
        dao.deleteAll(domains);
    }




}
