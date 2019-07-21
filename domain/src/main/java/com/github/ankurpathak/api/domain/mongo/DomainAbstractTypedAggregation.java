package com.github.ankurpathak.api.domain.mongo;

import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Domain;
import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class DomainAbstractTypedAggregation<T extends Domain<ID>, ID extends Serializable> extends AbstractTypedAggregation<T> {



    public DomainAbstractTypedAggregation(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
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


}
