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

    <S extends T> S create(S entity);

    <S extends T> S update(S entity);

    <S extends T> Iterable<S> createAll(Iterable<S> entities);


    void delete(T entity);

    void deleteById(ID id);

}
