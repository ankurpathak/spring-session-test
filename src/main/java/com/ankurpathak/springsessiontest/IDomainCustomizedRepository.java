package com.ankurpathak.springsessiontest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import java.io.Serializable;

public interface IDomainCustomizedRepository<T extends Domain<ID>, ID extends Serializable> {
    Page<T> findByField(String field, String value, Pageable pageable, Class<T> type);
    Page<String> listField(String field, String value, Pageable pageable, Class<T> type);
    Page<T> findByCriteria(Criteria criteria, Pageable pageable, Class<T> type);
}
