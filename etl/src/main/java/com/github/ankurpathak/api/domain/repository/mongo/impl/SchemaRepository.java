package com.github.ankurpathak.api.domain.repository.mongo.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.constant.SchemaConstants;
import com.github.ankurpathak.api.domain.repository.mongo.ISchemaRepository;
import org.bson.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository
public class SchemaRepository implements ISchemaRepository {

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    public SchemaRepository(MongoTemplate mongoTemplate, ObjectMapper objectMapper) {
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createViews() throws IOException {
        Resource json = new ClassPathResource(SchemaConstants.File.VIEW, this.getClass());
        Document views = objectMapper.readValue(json.getFile(), Document.class);
        views.keySet().forEach(key -> {
            mongoTemplate.dropCollection(key);
            Document view = new Document();
            view.putAll((Map) views.get(key));
            mongoTemplate.executeCommand(view);
        });
    }

    @Override
    public void createCollections() throws IOException {
        List<String> collections = new ArrayList<>();
        collections.addAll(Arrays.asList(SchemaConstants.COLLECTIONS));
        collections.addAll(mongoTemplate.getCollectionNames());
        collections.forEach(col -> {
            if(!mongoTemplate.collectionExists(col)){
                mongoTemplate.createCollection(col);
            }
        });
        ;
    }
}
