package com.github.ankurpathak.api.domain.mongo;

import com.github.ankurpathak.api.constant.Params;
import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;

import java.util.List;

public class AbstractAggregation {

    protected final MongoTemplate mongoTemplate;

    public AbstractAggregation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }




    protected long count(List<Document> documents){
        if(CollectionUtils.isNotEmpty(documents)){
            Document result = documents.get(0);
            return  ((Integer)result.get(Params.COUNT)).longValue();
        }else {
            return 0L;
        }
    }


    protected long getCount(Aggregation aggregation, String collection){
        List<Document> results = mongoTemplate.aggregate(aggregation, collection, Document.class).getMappedResults();
        return count(results);
    }


    protected List<Document> getList(Aggregation aggregation, String collection){
        return mongoTemplate.aggregate(aggregation, collection, Document.class).getMappedResults();
    }

}
