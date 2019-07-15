package com.github.ankurpathak.api.domain.mongo;

import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Domain;
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
        List<AggregationOperation> operations = new ArrayList<>(getCombinedAggregation(field, value));
        if (!pageable.getSort().isEmpty())
            operations.add(TypedAggregation.sort(pageable.getSort()));
        operations.add(Aggregation.skip(skip));
        operations.add(Aggregation.limit(limit));
        if(project)
            operations.add(Aggregation.project(field).andExclude(Params.MONGO_ID));

        return Aggregation.newAggregation(type, operations);
    }




    private TypedAggregation<T> getCountAggregation(String field, String value, Class<T> type) {
        List<AggregationOperation> operations = new ArrayList<>(getCombinedAggregation(field, value));
        operations.add(Aggregation.count().as(Params.COUNT));
        return Aggregation.newAggregation(type, operations);
    }


    public static List<AggregationOperation> getCombinedAggregation(String field, String value) {
        return List.of(anyFieldToString(field), search(value));
    }


    private static AggregationOperation search(String value) {
        return context -> new Document(OP_MATCH, new Document(Params.FIELD, new Document(
                OP_REGEX, value)
                .append(OP_OPTIONS, Params.I)
        ));
    }

    private static AggregationOperation anyFieldToString(String field) {
        return context -> new Document(OP_ADD_FIELDS, new Document(
                Params.FIELD, new Document(
                OP_TO_STRING, referField(field))));
    }


    public static final String OP_MATCH = "$match";
    public static final String OP_PROJECT = "$project";
    public static final String OP_CONCAT = "$concat";
    public static final String OP_ADD_FIELDS = "$addFields";
    public static final String OP_REGEX = "$regex";
    public static final String OP_OPTIONS = "$options";
    public static final String OP_CONVERT = "$convert";
    public static final String OP_TO_STRING = "$toString";


    public static final String FORMAT_FIETD = "$%s";


    private static String referField(String field) {
        return String.format(FORMAT_FIETD, field);
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
