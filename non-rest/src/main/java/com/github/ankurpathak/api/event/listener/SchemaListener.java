package com.github.ankurpathak.api.event.listener;

import com.github.ankurpathak.api.service.ISchemaService;
import com.github.ankurpathak.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class SchemaListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger log = LoggerFactory.getLogger(SchemaListener.class);

    @Autowired
    ISchemaService schemaService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try{
            schemaService.createCollections();
        }catch (Exception ex){
            LogUtil.logStackTrace(log, ex);
        }
    }


}
