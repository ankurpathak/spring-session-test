package com.ankurpathak.springsessiontest;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SingleFieldSearchAggregation<T extends Domain<ID>, ID extends Serializable> extends AbstractAggregation<T, ID> {


    public SingleFieldSearchAggregation(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }


    private TypedAggregation<T> getListAggregation(String field, String value, Pageable pageable, Class<T> type, boolean project) {
        long skip = (long) pageable.getPageNumber() * (long) pageable.getPageSize();
        long limit = pageable.getPageSize();
        List<AggregationOperation> operations = new ArrayList<>(getCombinedAggreagation(field, value));
        if (!pageable.getSort().isEmpty())
            operations.add(TypedAggregation.sort(pageable.getSort()));
        operations.add(Aggregation.skip(skip));
        operations.add(Aggregation.limit(limit));
        if(project)
            operations.add(Aggregation.project(field).andExclude("_id"));

        return Aggregation.newAggregation(type, operations);
    }




    private TypedAggregation<T> getCountAggregation(String field, String value, Class<T> type) {
        List<AggregationOperation> operations = new ArrayList<>(getCombinedAggreagation(field, value));
        operations.add(Aggregation.count().as("count"));
        return Aggregation.newAggregation(type, operations);
    }


    public static List<AggregationOperation> getCombinedAggreagation(String field, String value) {
        return List.of(anyFieldToString(field), search(value));
    }


    private static AggregationOperation search(String value) {
        return context -> new Document(MATCH, new Document("field", new Document(
                REGEX, value)
                .append(OPTIONS, "i")
        ));
    }

    private static AggregationOperation anyFieldToString(String field) {
        return context -> new Document(ADD_FIELDS, new Document(
                "field", new Document(
                TO_STRING, referField(field))));
    }


    public static final String MATCH = "$match";
    public static final String PROJECT = "$project";
    public static final String CONCAT = "$concat";
    public static final String ADD_FIELDS = "$addFields";
    public static final String REGEX = "$regex";
    public static final String OPTIONS = "$options";
    public static final String CONVERT = "$convert";
    public static final String TO_STRING = "$toString";


    private static String referField(String field) {
        return String.format("$%s", field);
    }


    public long getCount(String field, String value, Class<T> type) {
        return getCount(getCountAggregation(field, value, type), type);
    }



    public Page<T> getPage(String field, String value, Pageable pageable, Class<T> type) {
        List<T> list = getList(getListAggregation(field, value, pageable, type, false), type);
        long count = getCount(field, value, type);
        return new PageImpl<>(list, pageable, count);

    }


    public Page<String> getFieldPage(String field, String value, Pageable pageable, Class<T> type) {
        List<String> list = getFieldList(getListAggregation(field, value, pageable, type, true), type, field);
        long count = getCount(field, value, type);
        return new PageImpl<>(list, pageable, count);
    }


}
