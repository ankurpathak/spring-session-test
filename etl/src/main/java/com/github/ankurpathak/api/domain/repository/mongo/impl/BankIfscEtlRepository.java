package com.github.ankurpathak.api.domain.repository.mongo.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.constant.BankEtlConstants;
import com.github.ankurpathak.api.domain.repository.mongo.IBankIfscEltRepository;
import com.github.ankurpathak.api.util.PropertyUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("unchecked")
public class BankIfscEtlRepository implements IBankIfscEltRepository {
    private final MongoTemplate mongoTemplate;
    private final Environment environment;
    private final ObjectMapper objectMapper;

    public BankIfscEtlRepository(MongoTemplate mongoTemplate, Environment environment, ObjectMapper objectMapper) {
        this.mongoTemplate = mongoTemplate;
        this.environment = environment;
        this.objectMapper = objectMapper;
    }


    @Override
    public void process() throws IOException {
        File ifscDir = Paths.get(PropertyUtil.getProperty(environment, BankEtlConstants.Property.PATH)).toFile();

        if(ifscDir.isDirectory()){
            Collection<File> jsonFiles = FileUtils.listFiles(ifscDir, new String[]{"json"}, false);
            for (File jsonFile: jsonFiles){
                if(Objects.equals(String.format("%s.json", BankEtlConstants.File.BANK_NAMES), jsonFile.getName())){
                    processJsonKeyValueFile(jsonFile);
                }else if(Objects.equals(String.format("%s.json", BankEtlConstants.File.BANK), jsonFile.getName())){
                    processJsonKeyMapFile(jsonFile);
                }else if(Objects.equals(String.format("%s.json", BankEtlConstants.File.SUBLET), jsonFile.getName())){
                    processJsonKeyValueFile(jsonFile);
                }else if(Objects.equals(String.format("%s.json", BankEtlConstants.File.IFSC), jsonFile.getName())){
                    processJsonKeyValueFile(jsonFile);
                }else if(Objects.equals(String.format("%s.json", BankEtlConstants.File.IFSC_LIST), jsonFile.getName())){
                    processJsonListFile(jsonFile);
                }else {
                    processJsonKeyMapFile(jsonFile);
                }
            }

        }

    }


    private void processJsonListFile(File jsonFile) throws IOException {
        List<Object> jsonObj= objectMapper.readValue(jsonFile, List.class);
        Set<Object> jsonSet = jsonObj.stream().map(Function.identity()).collect(Collectors.toSet());
        Set<Document> documents = jsonSet.stream().map(value -> new Document("_id", value)).collect(Collectors.toSet());
        String collectionName = FilenameUtils.getBaseName(jsonFile.getAbsolutePath());
        collectionName = collectionName.replace("-","");
        mongoTemplate.dropCollection(collectionName);
        mongoTemplate.insert(documents, collectionName);
    }


    private void processJsonKeyMapFile(File jsonFile) throws IOException {
        Map<String, Map<String, Object>> jsonObj = objectMapper.readValue(jsonFile, Map.class);
        List<Map<String, Object>> documents = jsonObj.keySet().stream().map(key -> new Document(jsonObj.get(key)).append("_id", key)).collect(Collectors.toList());
        String collectionName = FilenameUtils.getBaseName(jsonFile.getAbsolutePath());
        mongoTemplate.dropCollection(collectionName);
        mongoTemplate.insert(documents, collectionName);
    }


    private void processJsonKeyValueFile(File jsonFile) throws IOException{
        Map<String, Object> jsonObj = objectMapper.readValue(jsonFile, Map.class);
        List<Document> documents = jsonObj.keySet().stream().map(key -> new Document("_id", key).append("value", jsonObj.get(key))).collect(Collectors.toList());
        String collectionName = FilenameUtils.getBaseName(jsonFile.getAbsolutePath());
        mongoTemplate.dropCollection(collectionName);
        mongoTemplate.insert(documents, collectionName);
    }
}
