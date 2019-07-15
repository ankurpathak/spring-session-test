package com.github.ankurpathak.api.domain.repository.mongo.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.constant.IfscConstants;
import com.github.ankurpathak.api.domain.repository.mongo.IBankIfscEltRepository;
import com.github.ankurpathak.api.util.PropertyUtil;
import com.google.common.collect.Maps;
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
        File ifscDir = Paths.get(PropertyUtil.getProperty(environment,IfscConstants.Property.PATH)).toFile();

        if(ifscDir.isDirectory()){
            Collection<File> jsonFiles = FileUtils.listFiles(ifscDir, new String[]{"json"}, false);
            for (File jsonFile: jsonFiles){
                if(Objects.equals(IfscConstants.File.BANK_NAMES, jsonFile.getName())){
                   processBankNames(jsonFile);
                }else if(Objects.equals(IfscConstants.File.BANK, jsonFile.getName())){
                    processBank(jsonFile);
                }else if(Objects.equals(IfscConstants.File.SUBLET, jsonFile.getName())){
                    processSublet(jsonFile);
                }
            }

        }

    }
    private void processSublet(File subletFile) throws IOException {
        Map<String, String> subletMap = objectMapper.readValue(subletFile, Map.class);
        List<Document> sublet = subletMap.keySet().stream().map(key -> new Document(key, subletMap.get(key))).collect(Collectors.toList());
        String subletCollection = FilenameUtils.getBaseName(subletFile.getAbsolutePath());
        mongoTemplate.dropCollection(subletCollection);
        mongoTemplate.insert(sublet, subletCollection);
    }

    private void processBank(File banksFile) throws IOException {
        Map<String, Map<String, Object>> banksMap = objectMapper.readValue(banksFile, Map.class);
        List<Map<String, Object>> banks = banksMap.keySet().stream().map(key -> new Document(key, banksMap.get(key))).collect(Collectors.toList());
        String banksCollection = FilenameUtils.getBaseName(banksFile.getAbsolutePath());
        mongoTemplate.dropCollection(banksCollection);
        mongoTemplate.insert(banks, banksCollection);
    }


    private void processBankNames(File bankNamesFile) throws IOException{
        Map<String, String> bankNamesMap = objectMapper.readValue(bankNamesFile, Map.class);
        List<Document> bankNames = bankNamesMap.keySet().stream().map(key -> new Document(key, bankNamesMap.get(key))).collect(Collectors.toList());
        String bankNamesCollection = FilenameUtils.getBaseName(bankNamesFile.getAbsolutePath());
        mongoTemplate.dropCollection(bankNamesCollection);
        mongoTemplate.insert(bankNames, bankNamesCollection);
    }
}
