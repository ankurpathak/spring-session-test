package com.github.ankurpathak.api.domain.repository.mongo.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.constant.SchemaConstants;
import com.github.ankurpathak.api.domain.repository.mongo.ISchemaRepository;
import org.apache.commons.collections4.MapUtils;
import org.bson.Document;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
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
}
