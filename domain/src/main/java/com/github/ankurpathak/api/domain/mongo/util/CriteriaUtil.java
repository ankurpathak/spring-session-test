package com.github.ankurpathak.api.domain.mongo.util;

import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.domain.model.BusinessExtendedDomain;
import com.github.ankurpathak.api.domain.model.Customer;
import com.github.ankurpathak.api.security.util.SecurityUtil;
import org.springframework.data.mongodb.core.query.Criteria;

import java.math.BigInteger;

public class CriteriaUtil {
    public static Criteria buildBusinessCriteria(Class<?> type){
        if(BusinessExtendedDomain.class.isAssignableFrom(type)){
            return Criteria.where(Model.BusinessExtendedDomain.Field.BUSINESS_ID).is(SecurityUtil.getRequestedMyBusiness().map(Business::getId).orElse(BigInteger.ZERO));
        }

        if(Customer.class.isAssignableFrom(type)){
            return Criteria.where(Model.Customer.Field.BUSINESS_ID).is(SecurityUtil.getRequestedMyBusiness().map(Business::getId).orElse(BigInteger.ZERO));
        }

        return new Criteria();
    }


    public static Criteria converToBusinessCriteria(Class<?> type, Criteria criteria){
        if(BusinessExtendedDomain.class.isAssignableFrom(type)){
            return criteria.and(Model.BusinessExtendedDomain.Field.BUSINESS_ID).is(SecurityUtil.getRequestedMyBusiness().map(Business::getId).orElse(BigInteger.ZERO));
        }

        if(Customer.class.isAssignableFrom(type)){
            return criteria.and(Model.Customer.Field.BUSINESS_ID).is(SecurityUtil.getRequestedMyBusiness().map(Business::getId).orElse(BigInteger.ZERO));
        }

        return criteria;
    }
}
