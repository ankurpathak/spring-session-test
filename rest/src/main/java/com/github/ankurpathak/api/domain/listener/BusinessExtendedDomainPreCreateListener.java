package com.github.ankurpathak.api.domain.listener;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.BusinessExtendedDomain;
import com.github.ankurpathak.api.security.dto.DomainContext;
import com.github.ankurpathak.api.security.util.SecurityUtil;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BusinessExtendedDomainPreCreateListener extends AbstractMongoEventListener<BusinessExtendedDomain<?>> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<BusinessExtendedDomain<?>> event) {
        Optional<DomainContext> context = SecurityUtil.getDomainContext();
        if(context.isPresent()){
            Business business = context.get().getBusiness();
            if(business != null){
                event.getSource().setBusinessId(business.getId());
            }
        }
    }
}
