package com.github.ankurpathak.api.domain.repository.mongo.impl;

import com.github.ankurpathak.api.constant.BankEtlConstants;
import com.github.ankurpathak.api.domain.repository.mongo.IBankRepository;
import com.github.ankurpathak.api.util.MatcherUtil;
import com.mongodb.client.DistinctIterable;
import org.apache.commons.collections.IteratorUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.valid4j.Assertive.require;

@Repository
public class BankRepository implements IBankRepository {
    private final MongoTemplate mongoTemplate;

    public BankRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<Document> findNames() {
        return mongoTemplate.find(new Query(), Document.class, BankEtlConstants.File.BANK_NAMES);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> findStates(String code) {
        Optional<String> ifsc = findIfsc(code);
        if(ifsc.isPresent()){
            DistinctIterable<String> result = mongoTemplate.getCollection(ifsc.get()).distinct(BankEtlConstants.Columns.STATE, String.class);
            return IteratorUtils.toList(result.iterator(), 50);
        }else {
            return Collections.emptyList();
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<String> findDistrict(String code, String state) {
        Optional<String> ifsc = findIfsc(code);
        if(ifsc.isPresent()){
            DistinctIterable<String> result = mongoTemplate.getCollection(ifsc.get()).distinct(BankEtlConstants.Columns.DISTRICT, new Document(BankEtlConstants.Columns.STATE, state), String.class);
            return IteratorUtils.toList(result.iterator(), 100);
        }else {
            return Collections.emptyList();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Document> findBranches(String code, String state, String district) {
        Optional<String> ifsc = findIfsc(code);
        if(ifsc.isPresent()){
            Query query = new Query();
            query.addCriteria(Criteria.where(BankEtlConstants.Columns.STATE).is(state).andOperator(Criteria.where(BankEtlConstants.Columns.DISTRICT).is(district)));
            return mongoTemplate.find(query, Document.class, ifsc.get());
        }else {
            return Collections.emptyList();
        }
    }


    @Override
    public Optional<Document> findBranch(String ifsc){
        require(ifsc, MatcherUtil.notStringEmpty());
        Query query = new Query();
        query.addCriteria(Criteria.where(BankEtlConstants.Columns.ID).is(ifsc));
        boolean isValid = mongoTemplate.exists(query, BankEtlConstants.File.IFSC_LIST.replace("-", ""));
        if(isValid){
            query = new Query();
            query.addCriteria(Criteria.where(BankEtlConstants.Columns.IFSC.toUpperCase()).is(ifsc));
            return Optional.ofNullable(mongoTemplate.findOne(query, Document.class, ifsc.substring(0, 4)));
        }
        return Optional.empty();
    }


    private Optional<String> findIfsc(String code){
        Criteria criteria = Criteria.where(BankEtlConstants.Columns.ID).is(code);
        Query query = new Query();
        query.fields().exclude(BankEtlConstants.Columns.ID).include(BankEtlConstants.Columns.IFSC);
        query.addCriteria(criteria);
        Document ifscDocument = mongoTemplate.findOne(query, Document.class, BankEtlConstants.File.BANK);
        return Optional.ofNullable(ifscDocument)
                .map(doc -> doc.getString(BankEtlConstants.Columns.IFSC)).filter(ifsc -> ifsc.length() > 4).map(ifsc-> ifsc.substring(0, 4));
    }


}
