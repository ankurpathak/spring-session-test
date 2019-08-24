package com.github.ankurpathak.api.domain.repository.mongo.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.domain.repository.mongo.ICountryEtlRepository;
import org.bson.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CountryEtlRepository implements ICountryEtlRepository {

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    public CountryEtlRepository(MongoTemplate mongoTemplate, ObjectMapper objectMapper) {
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void process() throws IOException {
        ClassPathResource resource = new ClassPathResource("country/country.json", this.getClass());
        List<Map<String, Object>> jsonObj = objectMapper.readValue(resource.getFile(), List.class);
        List<Document> docs = jsonObj.stream().map(x -> {
            Document doc = new Document();
            doc.putAll(x);
            return doc;
        }).collect(Collectors.toList());
        mongoTemplate.insert(docs, "country");
    }
}
