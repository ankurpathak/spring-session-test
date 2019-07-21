package com.github.ankurpathak.api.domain.mongo;

import com.github.ankurpathak.api.constant.CityEtlConstants;
import com.github.ankurpathak.api.constant.Params;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SingleFieldDistinctSort<T> extends AbstractAggregation {
    public SingleFieldDistinctSort(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }


    private Aggregation getListAggregation(Criteria criteria, Sort sort, String field) {
        List<AggregationOperation> operations = getCombinedAggregation(criteria, field);
        if(!sort.isEmpty()){
            operations.add(Aggregation.sort(sort));
        }
        return Aggregation.newAggregation(operations);
    }


    private List<AggregationOperation> getCombinedAggregation(Criteria criteria, String field){
        List<AggregationOperation> operations = new ArrayList<>();
        if(criteria != null)
        operations.add(Aggregation.match(criteria));
        operations.add(Aggregation.group(field));
        return operations;
    }



    private Aggregation getPageAggregation(Criteria criteria, Pageable pageable, String field) {
        List<AggregationOperation> operations = getCombinedAggregation(criteria, field);
        if(!pageable.getSort().isEmpty()){
            operations.add(Aggregation.sort(pageable.getSort()));
        }
        long skip = (long) pageable.getPageNumber() * (long) pageable.getPageSize();
        long limit = pageable.getPageSize();
        operations.add(Aggregation.skip(skip));
        operations.add(Aggregation.limit(limit));
        return Aggregation.newAggregation(operations);
    }




    private Aggregation getCountAggregation(Criteria criteria, String field) {
        List<AggregationOperation> operations = getCombinedAggregation(criteria, field);
        operations.add(Aggregation.count().as(Params.COUNT));
       return Aggregation.newAggregation(operations);
    }




    public long getCount(Criteria criteria, String field, String collection) {
        return getCount(getCountAggregation(criteria, field),collection);
    }


    public Page<Document> getPage(Criteria criteria, Pageable pageable, String field, String collection) {
        List<Document> list = getList(getPageAggregation(criteria, pageable, field), collection);
        long count = getCount(criteria,field, collection);
        return new PageImpl<>(list, pageable, count);

    }


    public Page<String> getFieldPage(Criteria criteria, Pageable pageable, String field, String collection) {
        List<Document> list = getList(getPageAggregation(criteria, pageable, field), collection);
        List<String> fields = list.stream().map(doc -> doc.getString(CityEtlConstants.Columns.ID)).collect(Collectors.toList());
        long count = getCount(criteria,field, collection);
        return new PageImpl<>(fields, pageable, count);

    }


    public List<Document> getList(Criteria criteria, Sort sort, String field, String collection) {
        return getList(getListAggregation(criteria, sort, field), collection);
    }

    public List<String> getFieldList(Criteria criteria, Sort sort, String field, String collection) {
        List<Document> list = getList(getListAggregation(criteria, sort, field), collection);
        return list.stream().map(doc -> doc.getString(CityEtlConstants.Columns.ID)).collect(Collectors.toList());
    }







}
