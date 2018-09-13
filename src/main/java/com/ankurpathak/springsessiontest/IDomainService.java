package com.ankurpathak.springsessiontest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IDomainService<T extends Domain<ID>, ID extends Serializable> {

    // read - one

    Optional<T> findById(ID id);

    // read - all

    List<T> findAll();

    Page<T> findPaginated(Pageable pageable);

    // write

    T create(T entity);

    T update(T entity);

    Iterable<T> createAll(Iterable<T> entities);


    void delete(T entity);

    void deleteById(ID id);

    String domainName();

    Page<T> search(String field, String value, Pageable pageable, Class<T> type);
    Page<String> listField(String field, String value, Pageable pageable, Class<T> type);

}
