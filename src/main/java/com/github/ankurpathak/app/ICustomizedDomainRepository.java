package com.github.ankurpathak.app;

import com.github.ankurpathak.app.domain.model.Domain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import java.io.Serializable;
import java.util.stream.Stream;

public interface ICustomizedDomainRepository<T extends Domain<ID>, ID extends Serializable> {
    Page<T> findByField(String field, String value, Pageable pageable, Class<T> type);
    Page<String> listField(String field, String value, Pageable pageable, Class<T> type);
    Page<T> findByCriteriaPaginated(Criteria criteria, Pageable pageable, Class<T> type);
    Stream<T> findByCriteria(Criteria criteria, Pageable pageable, Class<T> type);
    long countByCriteria(Criteria criteria, Class<T> type);
}
