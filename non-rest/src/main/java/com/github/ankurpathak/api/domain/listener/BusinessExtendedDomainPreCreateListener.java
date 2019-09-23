package com.github.ankurpathak.api.domain.listener;

import com.github.ankurpathak.api.batch.task.ITaskContext;
import com.github.ankurpathak.api.batch.task.TaskContextHolder;
import com.github.ankurpathak.api.domain.model.BusinessExtendedDomain;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

@Component
public class BusinessExtendedDomainPreCreateListener extends AbstractMongoEventListener<BusinessExtendedDomain<?>> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<BusinessExtendedDomain<?>> event) {
        TaskContextHolder.getContext()
                .map(ITaskContext::getBusiness)
                .ifPresent( business -> {
                    if(business != null){
                        event.getSource().setBusinessId(business.getId());
                    }
                });
    }
}
