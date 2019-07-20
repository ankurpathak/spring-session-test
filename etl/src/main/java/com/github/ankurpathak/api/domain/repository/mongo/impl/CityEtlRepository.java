package com.github.ankurpathak.api.domain.repository.mongo.impl;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.ankurpathak.api.constant.CityEtlConstants;
import com.github.ankurpathak.api.domain.repository.mongo.ICityEtlRepository;
import com.github.ankurpathak.api.util.PropertyUtil;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

@Repository
public class CityEtlRepository implements ICityEtlRepository {

    private final MongoTemplate mongoTemplate;
    private final Environment environment;

    public CityEtlRepository(MongoTemplate mongoTemplate, Environment environment) {
        this.mongoTemplate = mongoTemplate;
        this.environment = environment;
    }

    @Override
    public void process() throws IOException {
        File file = Paths.get(PropertyUtil.getProperty(environment, CityEtlConstants.Property.PATH)).toFile();
        try(FileReader reader = new FileReader(file)){
            Iterator<Map<String, String>> iterator = new CsvMapper()
                    .readerFor(Map.class)
                    .with(CsvSchema.emptySchema().withHeader())
                    .readValues(reader);
            mongoTemplate.getCollection(CityEtlConstants.Collection.CITY).drop();
            while (iterator.hasNext()) {
                Map<String, String> keyValues = iterator.next();
                mongoTemplate.insert(keyValues, CityEtlConstants.Collection.CITY);

            }
        }


    }
}
