package com.ankurpathak.springsessiontest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    public T create(T entity) {
        return getDao().insert(entity);
    }

    @Override
    public T update(T entity) {
        return getDao().save(entity);
    }

    @Override
    public Iterable<T> createAll(Iterable<T> entities) {
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

    protected abstract ExtendedRepository<T, ID> getDao();

    @Override
    public String domainName(){
        String name = this.getClass().getSimpleName();
        name = name != null ? name : "";
        int index = name.indexOf('S');
        index = index > -1 ? index : 1;
        return name.substring(1, index).trim();
    }


    @Override
    public Page<T> search(String field, String value, Pageable pageable, Class<T> type) {
        return getDao().search(field, value, pageable, type);
    }

    @Override
    public Page<String> listField(String field, String value, Pageable pageable, Class<T> type) {
        return getDao().listField(field, value, pageable, type);
    }
}
