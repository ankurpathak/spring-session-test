package com.github.ankurpathak.api.batch.item.writer.listener;

import com.github.ankurpathak.api.domain.model.Domain;
import org.springframework.batch.core.ItemWriteListener;

import java.io.Serializable;
import java.util.List;


public class DomainItemWriteListener<T extends Domain<ID>, ID extends Serializable> implements ItemWriteListener<T> {
    private final Class<T> tClass;

    public DomainItemWriteListener(Class<T> tClass) {
        this.tClass = tClass;
    }




    @Override
    public void beforeWrite(List<? extends T> items) {

    }

    @Override
    public void afterWrite(List<? extends T> items) {

    }

    @Override
    public void onWriteError(Exception exception, List<? extends T> items) {

    }
}
