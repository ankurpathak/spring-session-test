package com.github.ankurpathak.api.domain.mongo.util;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.security.util.SecurityUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import java.math.BigInteger;

public class CriteriaUtil {
    public static Criteria businessCiteria(Criteria criteria, Class<?> type){
        if(Model.LIST_BUSINESS_DOMAIN.contains(type)){
            criteria.and(Model.Domain.Field.BUSINESS_ID).is(SecurityUtil.getRequestedMyBusiness().map(Business::getId).orElse(BigInteger.ZERO));
        }

        MongoTemplate template;
        
        return criteria;
    }
}
