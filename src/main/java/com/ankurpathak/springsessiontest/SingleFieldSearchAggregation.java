package com.ankurpathak.springsessiontest;

import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class SingleFieldSearchAggregation<T extends Domain<ID>, ID extends Serializable> {

    private final MongoTemplate mongoTemplate;

    public SingleFieldSearchAggregation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private static final String SORT = "$sort";

    private static Aggregation getListAggreagation(String field, String value, Pageable pageable){
        long skip = (long)pageable.getPageNumber() * (long)pageable.getPageSize();
        long limit = pageable.getPageSize();
        Optional<Sort.Order> order = pageable.getSort().stream().findFirst();
        List<AggregationOperation> operations = new ArrayList<>(getCombinedAggreagation(field, value));
        order.ifPresent(x -> operations.add(sort(x.getProperty(), x.getDirection().isAscending() ? 1 : -1)));
        operations.add(Aggregation.skip(skip));
        operations.add(Aggregation.limit(limit));
        return Aggregation.newAggregation();
    }


    public static AggregationOperation sort(String key, int direction){
        return context -> new Document(SORT, new Document(key, direction));
    }



    public static Aggregation getCountAggregation(String field, String value){
        List<AggregationOperation> operations = new ArrayList<>(getCombinedAggreagation(field, value));
        operations.add(Aggregation.count().as("count"));
        return Aggregation.newAggregation(operations);
    }


    public static List<AggregationOperation> getCombinedAggreagation(String field, String value){
        return List.of(anyFieldToString(field),search(value));
    }





    private static AggregationOperation search(String value){
        return context ->  new Document(MATCH, new Document("field", new Document(
                REGEX, String.format(".*%s.*", value))
                .append(OPTIONS, "i")
        ));
    }






    private static AggregationOperation  anyFieldToString(String field) {
        return context ->  new Document(ADD_FIELDS, new Document(
                                                 "field",  new Document(
                                                           CONCAT,  List.of("", field))));
    }


    public static final String MATCH = "$match";
    public static final String PROJECT = "$project";
    public static final String CONCAT = "$concat";
    public static final String ADD_FIELDS = "$addFields";
    public static final String REGEX = "$regex";
    public static final String OPTIONS = "$options";


    private static String referField(String field){
        return String.format("$%s", field);
    }


    public long getCount(String field, String value, Class<T> type){
        List<Document> results = mongoTemplate.aggregate(getCountAggregation(field, value), type, Document.class).getMappedResults();
        if(CollectionUtils.isNotEmpty(results)){
            Document result = results.get(0);
            return  ((Integer)result.get("count")).longValue();
        }else {
            return 0L;
        }
    }


    public List<T> getList(String field, String value, Pageable pageable, Class<T> type){
        return mongoTemplate.aggregate(getListAggreagation(field, value, pageable), type, type).getMappedResults();
    }


}
