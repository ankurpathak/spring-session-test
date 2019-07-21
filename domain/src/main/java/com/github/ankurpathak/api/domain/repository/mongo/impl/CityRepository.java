package com.github.ankurpathak.api.domain.repository.mongo.impl;

import com.github.ankurpathak.api.constant.CityEtlConstants;
import com.github.ankurpathak.api.domain.mongo.SingleFieldDistinctSort;
import com.github.ankurpathak.api.domain.repository.mongo.ICityRepository;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CityRepository implements ICityRepository {
    private final MongoTemplate mongoTemplate;

    public CityRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> findStates(){
        SingleFieldDistinctSort<Document> singleFieldDistinctSort = new SingleFieldDistinctSort<>(mongoTemplate);
        return singleFieldDistinctSort.getFieldList(
                null,
                Sort.by(Sort.Order.asc(CityEtlConstants.Columns.ID)),
                CityEtlConstants.Columns.STATE,
                CityEtlConstants.Collection.CITY
        );
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<String> findDistricts(String state){
        SingleFieldDistinctSort<Document> singleFieldDistinctSort = new SingleFieldDistinctSort<>(mongoTemplate);
        return singleFieldDistinctSort.getFieldList(
                Criteria.where(CityEtlConstants.Columns.STATE).is(state),
                Sort.by(Sort.Order.asc(CityEtlConstants.Columns.ID)),
                CityEtlConstants.Columns.DISTRICT,
                CityEtlConstants.Collection.CITY
        );
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<String> findDistricts(){
        SingleFieldDistinctSort<Document> singleFieldDistinctSort = new SingleFieldDistinctSort<>(mongoTemplate);
        return singleFieldDistinctSort.getFieldList(
                null,
                Sort.by(Sort.Order.asc(CityEtlConstants.Columns.ID)),
                CityEtlConstants.Columns.DISTRICT,
                CityEtlConstants.Collection.CITY
        );
    }




    @Override
    public Page<String> findPinCodes(String state, String district, Pageable pageable){
        SingleFieldDistinctSort<Document> singleFieldDistinctSort = new SingleFieldDistinctSort<>(mongoTemplate);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.asc(CityEtlConstants.Columns.ID)));
       return singleFieldDistinctSort.getFieldPage(
                Criteria.where(CityEtlConstants.Columns.STATE).is(state).and(CityEtlConstants.Columns.DISTRICT).is(district),
                sortedPageable,
                CityEtlConstants.Columns.PINCODE,
                CityEtlConstants.Collection.CITY
        );
    }


    @Override
    public Optional<Document> findPinCode(String pinCode){
        Query query = new Query();
        query.addCriteria(Criteria.where(CityEtlConstants.Columns.PINCODE).is(pinCode))
        .fields().exclude(CityEtlConstants.Columns.ID);
        return Optional.ofNullable(mongoTemplate.findOne(query, Document.class, CityEtlConstants.Collection.CITY));
    }




}
