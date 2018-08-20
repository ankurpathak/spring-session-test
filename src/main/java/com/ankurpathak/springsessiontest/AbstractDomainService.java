package com.ankurpathak.springsessiontest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDomainService<T extends Domain<ID>, ID extends Serializable> implements IDomainService<T, ID> {
    @Override
    public Optional<T> findById(final ID id) {
        return getDao().findById(id);
    }
    @Override
    public List<T> findAll() {
        return getDao().findAll();
    }

    @Override
    public Page<T> findPaginated(final Pageable pageable) {
        return getDao().findAll(pageable);
    }

    @Override
    public <S extends T> S create(S entity) {
        return getDao().save(entity);
    }

    @Override
    public <S extends T> S update(S entity) {
        return getDao().insert(entity);
    }

    @Override
    public <S extends T> Iterable<S> createAll(Iterable<S> entities) {
        return getDao().insert(entities);
    }

    @Override
    public void delete(final T entity) {
        getDao().delete(entity);
    }

    @Override
    public void deleteById(ID id) {
        getDao().deleteById(id);
    }

    protected abstract MongoRepository<T, ID> getDao();

}
