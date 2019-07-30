package com.github.ankurpathak.api.domain.listener;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.domain.model.ExtendedDomain;
import com.github.ankurpathak.api.domain.model.User;
import com.github.ankurpathak.api.security.util.SecurityUtil;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Component
public class ExtendedDomainAuditListener extends AbstractMongoEventListener<ExtendedDomain<?>> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<ExtendedDomain<?>> event) {
        ExtendedDomain<?> domain = event.getSource();
        if(WHITE_LISTED_DOMAIN.contains(domain.getClass())){
            Document doc = event.getDocument();
            if(Objects.isNull(domain.getCreated())){
                event.getSource().created(domain.getUpdated());
                if(doc!= null){
                    doc.append("created", domain.getCreated());
                }

            }
            if(Objects.isNull(domain.getCreatedBy())){
                event.getSource().createdBy(domain.getUpdatedBy());
                if(doc!= null){
                    doc.append("createdBy", String.valueOf(domain.getCreatedBy()));
                }
            }
        }
    }

    public static final List<Class<?>> WHITE_LISTED_DOMAIN = List.of(
            User.class,
            Customer.class,
            Business.class
    );
}
