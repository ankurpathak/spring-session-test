package com.ankurpathak.springsessiontest;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class SingleFieldSearchAggregation<T extends Domain<ID>, ID extends Serializable> extends AbstractAggregation<T, ID> {


    public SingleFieldSearchAggregation(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }


    private TypedAggregation<T> getListAggreagation(String field, String value, Pageable pageable, Class<T> type, boolean project) {
        long skip = (long) pageable.getPageNumber() * (long) pageable.getPageSize();
        long limit = pageable.getPageSize();
        Optional<Sort.Order> order = pageable.getSort().stream().findFirst();
        List<AggregationOperation> operations = new ArrayList<>(getCombinedAggreagation(field, value, project));
        order.ifPresent(x -> operations.add(sort(x.getProperty(), x.getDirection().isAscending() ? 1 : -1)));
        operations.add(Aggregation.skip(skip));
        operations.add(Aggregation.limit(limit));
        return Aggregation.newAggregation(type, operations);
    }


    private TypedAggregation<T> getListAggreagation(String field, String value, Pageable pageable, Class<T> type) {
        return getListAggreagation(field, value, pageable, type, false);
    }


    private TypedAggregation<T> getCountAggregation(String field, String value, Class<T> type) {
        return getCountAggregation(field, value, type, false);
    }


    private TypedAggregation<T> getCountAggregation(String field, String value, Class<T> type, boolean project) {
        List<AggregationOperation> operations = new ArrayList<>(getCombinedAggreagation(field, value, project));
        operations.add(Aggregation.count().as("count"));
        return Aggregation.newAggregation(type, operations);
    }


    public static List<AggregationOperation> getCombinedAggreagation(String field, String value, boolean project) {
        return List.of(anyFieldToString(field, project), search(value));
    }


    private static AggregationOperation search(String value) {
        return context -> new Document(MATCH, new Document("field", new Document(
                REGEX, String.format(".*%s.*", value))
                .append(OPTIONS, "i")
        ));
    }


    private static AggregationOperation anyFieldToString(String field, boolean project) {
        if (!project)
            return context -> new Document(ADD_FIELDS, new Document(
                    "field", new Document(
                    CONCAT, List.of("", referField(field)))));
        else
            return context -> new Document(PROJECT, new Document(
                    "field", new Document(
                    CONCAT, List.of("", referField(field)))).append("_id", false));

    }


    public static final String MATCH = "$match";
    public static final String PROJECT = "$project";
    public static final String CONCAT = "$concat";
    public static final String ADD_FIELDS = "$addFields";
    public static final String REGEX = "$regex";
    public static final String OPTIONS = "$options";


    private static String referField(String field) {
        return String.format("$%s", field);
    }


    public long getCount(String field, String value, Class<T> type) {
        if (Objects.equals("**", value))
            return getCount(getCountAggregation(field, "", type), type);
        return getCount(getCountAggregation(field, value, type), type);
    }

    public long getFieldCount(String field, String value, Class<T> type) {
        if (Objects.equals("**", value))
            return getCount(getCountAggregation(field, "", type, true), type);
        return getCount(getCountAggregation(field, value, type, true), type);
    }


    public Page<T> getPage(String field, String value, Pageable pageable, Class<T> type) {
        List<T> list = Objects.equals("**", value) ? getList(getListAggreagation(field, "", pageable, type), type) : getList(getListAggreagation(field, value, pageable, type), type);
        long count = getCount(field, value, type);
        return new PageImpl<>(list, pageable, count);

    }


    public Page<String> getFieldPage(String field, String value, Pageable pageable, Class<T> type) {
        List<String> list = Objects.equals("**", value) ? getFieldList(getListAggreagation(field, "", pageable, type, true), type): getFieldList(getListAggreagation(field, value, pageable, type, true), type);
        long count = getFieldCount(field, value, type);
        return new PageImpl<>(list, pageable, count);
    }


}
