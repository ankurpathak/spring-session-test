package com.github.ankurpathak.api.service;

import com.github.ankurpathak.api.domain.model.Domain;
import com.mongodb.bulk.BulkWriteResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IDomainService<T extends Domain<ID>, ID extends Serializable> {

    // read - one

    Optional<T> findById(ID id);

    // read - all

    List<T> findAll();

    List<T> findByCriteria(Criteria criteria, Pageable pageable, Class<T> type);

    long countByCriteria(Criteria criteria, Pageable pageable, Class<T> type);

    Page<T> findAllPaginated(Pageable pageable, Class<T> type);

    Page<T> findByCriteriaPaginated(Criteria criteria, Pageable pageable, Class<T> type);

    // write

    T create(T entity);

    T update(T entity);

    Iterable<T> createAll(Iterable<T> entities);


    void delete(T entity);

    void deleteById(ID id);

    String domainName();

    Page<T> findByField(String field, String value, Pageable pageable, Class<T> type);
    Page<String> listField(String field, String value, Pageable pageable, Class<T> type);


    void deleteAll(Iterable<T> domains);

    void deleteAll();

    BulkWriteResult bulkInsertMany(Class<T> type, List<T> domains);


    <S extends T> Page<S> findAllPaginated(Pageable pageable, Class<S> type, String view);

    long countByCriteria(Criteria criteria, Class<T> type, String view);

    List<T> findByCriteria(Criteria criteria, Pageable pageable, Class<T> type, String view);

    <S extends T> Page<S> findByCriteriaPaginated(Criteria criteria, Pageable pageable, Class<S> type, String view);
}
