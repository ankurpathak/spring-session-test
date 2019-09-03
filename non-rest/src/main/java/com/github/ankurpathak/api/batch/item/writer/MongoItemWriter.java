package com.github.ankurpathak.api.batch.item.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MongoItemWriter<T> implements ItemWriter<T>, InitializingBean {

    private MongoOperations template;
    private final Object bufferKey;
    private String collection;
    private boolean delete = false;

    public MongoItemWriter() {
        super();
        this.bufferKey = new Object();
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }


    public void setTemplate(MongoOperations template) {
        this.template = template;
    }

    protected MongoOperations getTemplate() {
        return template;
    }

        public void setCollection(String collection) {
        this.collection = collection;
    }


    @Override
    public void write(List<? extends T> items) throws Exception {
        if(!transactionActive()) {
            doWrite(items);
            return;
        }

        List<T> bufferedItems = getCurrentBuffer();
        bufferedItems.addAll(items);
    }


    protected void doWrite(List<? extends T> items) {
        if(! CollectionUtils.isEmpty(items)) {
            if(delete) {
                if(StringUtils.hasText(collection)) {
                    for (Object object : items) {
                        template.remove(object, collection);
                    }
                }
                else {
                    for (Object object : items) {
                        template.remove(object);
                    }
                }
            }
            else {
                if(StringUtils.hasText(collection)) {
                    for (Object object : items) {
                        template.insert(object, collection);
                    }
                }
                else {
                    for (Object object : items) {
                        template.insert(object);
                    }
                }
            }
        }
    }

    private boolean transactionActive() {
        return TransactionSynchronizationManager.isActualTransactionActive();
    }

    @SuppressWarnings("unchecked")
    private List<T> getCurrentBuffer() {
        if(!TransactionSynchronizationManager.hasResource(bufferKey)) {
            TransactionSynchronizationManager.bindResource(bufferKey, new ArrayList<T>());

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void beforeCommit(boolean readOnly) {
                    List<T> items = (List<T>) TransactionSynchronizationManager.getResource(bufferKey);

                    if(!CollectionUtils.isEmpty(items)) {
                        if(!readOnly) {
                            doWrite(items);
                        }
                    }
                }

                @Override
                public void afterCompletion(int status) {
                    if(TransactionSynchronizationManager.hasResource(bufferKey)) {
                        TransactionSynchronizationManager.unbindResource(bufferKey);
                    }
                }
            });
        }

        return (List<T>) TransactionSynchronizationManager.getResource(bufferKey);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(template != null, "A MongoOperations implementation is required.");
    }
}

