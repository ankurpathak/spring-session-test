package com.github.ankurpathak.api.domain.repository.mongo.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.constant.BankEtlConstants;
import com.github.ankurpathak.api.domain.repository.mongo.IBankEltRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("unchecked")
public class BankEtlRepository implements IBankEltRepository {
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    public BankEtlRepository(MongoTemplate mongoTemplate, ObjectMapper objectMapper) {
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = objectMapper;
    }


    @Override
    public void process() throws IOException {
       // File ifscDir = Paths.get(PropertyUtil.getProperty(environment, BankEtlConstants.Property.PATH)).toFile();

        ClassPathResource bankResource = new ClassPathResource("bank", this.getClass());

        if(bankResource.getFile().isDirectory()){
            Collection<File> jsonFiles = FileUtils.listFiles(bankResource.getFile(), new String[]{"json"}, false);
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
        Set<Object> jsonSet = new HashSet<>(jsonObj);
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
