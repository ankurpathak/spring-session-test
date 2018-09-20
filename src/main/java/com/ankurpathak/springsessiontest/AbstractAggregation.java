package com.ankurpathak.springsessiontest;

import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractAggregation<T extends Domain<ID>, ID extends Serializable> {

    private final MongoTemplate mongoTemplate;

    public AbstractAggregation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public AggregationOperation sort(String key, int direction) {
        return context -> new Document(SORT, new Document(key, direction));
    }





    public static final String SORT = "$sort";


    public static final String MATCH = "$match";
    public static final String PROJECT = "$project";
    public static final String CONCAT = "$concat";
    public static final String ADD_FIELDS = "$addFields";
    public static final String REGEX = "$regex";
    public static final String OPTIONS = "$options";



    private static String referField(String field){
        return String.format("$%s", field);
    }



    public long getCount(TypedAggregation<T> aggregation, Class<T> type){
        List<Document> results = mongoTemplate.aggregate(aggregation, type, Document.class).getMappedResults();
        if(CollectionUtils.isNotEmpty(results)){
            Document result = results.get(0);
            return  ((Integer)result.get("count")).longValue();
        }else {
            return 0L;
        }
    }


    public List<T> getList(TypedAggregation<T> aggregation, Class<T> type){
        return mongoTemplate.aggregate(aggregation, type, type).getMappedResults();
    }


    public List<String> getFieldList(TypedAggregation<T> aggregation, Class<T> type, String field){
       List<Document> results =  mongoTemplate.aggregate(aggregation, type, Document.class).getMappedResults();
       return results.stream().map(x -> x.getString(field)).collect(Collectors.toList());

    }







}
