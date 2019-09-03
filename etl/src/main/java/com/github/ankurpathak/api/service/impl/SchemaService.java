package com.github.ankurpathak.api.service.impl;

import com.github.ankurpathak.api.domain.repository.mongo.ISchemaRepository;
import com.github.ankurpathak.api.service.ISchemaService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.util.AnnotatedTypeScanner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Service
public class SchemaService implements ISchemaService {
    private final ISchemaRepository schemaRepository;

    public SchemaService(ISchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }


    @Override
    public void createViews() throws IOException {
        schemaRepository.createViews();
    }


    @Override
    public void createCollections() throws IOException {
        schemaRepository.createCollections();
    }


    @Override
    public Collection<Class<?>> getCollections(){
        return schemaRepository.getCollections();
    }


}
