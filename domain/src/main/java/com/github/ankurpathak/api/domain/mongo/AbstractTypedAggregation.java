package com.github.ankurpathak.api.domain.mongo;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;

import java.util.List;
import java.util.stream.Collectors;

public class AbstractTypedAggregation<T>  extends AbstractAggregation {

    protected AbstractTypedAggregation(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    protected List<T> getList(TypedAggregation<T> aggregation, Class<T> type){
        return mongoTemplate.aggregate(aggregation, type, type).getMappedResults();
    }


    protected List<String> getFieldList(TypedAggregation<T> aggregation, Class<T> type, String field){
        List<Document> results =  mongoTemplate.aggregate(aggregation, type, Document.class).getMappedResults();
        return results.stream().map(x -> x.getString(field)).collect(Collectors.toList());
    }

    protected long getCount(TypedAggregation<T> aggregation, Class<T> type){
        List<Document> results = mongoTemplate.aggregate(aggregation, type, Document.class).getMappedResults();
        return count(results);
    }
}
