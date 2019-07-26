package com.github.ankurpathak.api.domain.repository.mongo;

import com.github.ankurpathak.api.domain.model.Domain;
import com.mongodb.bulk.BulkWriteResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

public interface ICustomizedDomainRepository<T extends Domain<ID>, ID extends Serializable> {
    Page<T> findByField(String field, String value, Pageable pageable, Class<T> type);
    Page<String> listField(String field, String value, Pageable pageable, Class<T> type);
    Page<T> findByCriteriaPaginated(Criteria criteria, Pageable pageable, Class<T> type);
    Stream<T> findByCriteria(Criteria criteria, Pageable pageable, Class<T> type);
    long countByCriteria(Criteria criteria, Class<T> type);
    BulkWriteResult bulkInsertMany(Class<T> type, List<T> domains);
}
