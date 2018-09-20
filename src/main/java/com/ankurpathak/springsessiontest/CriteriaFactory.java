package com.ankurpathak.springsessiontest;


import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Objects;

public class CriteriaFactory {


    public static Criteria create(ComparisonNode node, String resource) {
        if (node == null)
            return null;
        List<?> castedArguments = FilterFieldMapper.castFilterField(resource, node.getSelector(), node.getArguments());

        if (Objects.equals(node.getOperator(), RSQLOperators.EQUAL)) {
            Object arg = castedArguments.get(0);
            if (arg instanceof String) {
                return Criteria.where(node.getSelector()).regex(String.valueOf(arg), "i");
            }
            return Criteria.where(node.getSelector()).is(castedArguments.get(0));
        } else if (Objects.equals(node.getOperator(), RSQLOperators.NOT_EQUAL)) {
            return Criteria.where(node.getSelector()).not().is(castedArguments.get(0));
        } else if (Objects.equals(node.getOperator(), RSQLOperators.GREATER_THAN)) {
            return Criteria.where(node.getSelector()).gt(castedArguments.get(0));
        } else if (Objects.equals(node.getOperator(), RSQLOperators.GREATER_THAN_OR_EQUAL)) {
            return Criteria.where(node.getSelector()).gte(castedArguments.get(0));
        } else if (Objects.equals(node.getOperator(), RSQLOperators.LESS_THAN)) {
            return Criteria.where(node.getSelector()).lt(castedArguments.get(0));
        } else if (Objects.equals(node.getOperator(), RSQLOperators.LESS_THAN_OR_EQUAL)) {
            return Criteria.where(node.getSelector()).lte(castedArguments.get(0));
        } else if (Objects.equals(node.getOperator(), RSQLOperators.IN)) {
            return Criteria.where(node.getSelector()).in(castedArguments);
        } else if (Objects.equals(node.getOperator(), RSQLOperators.NOT_IN)) {
            return Criteria.where(node.getSelector()).not().in(castedArguments);
        } else if (Objects.equals(node.getOperator(), CustomRSQLOperators.BETWEEN)) {
            return Criteria.where(node.getSelector()).gte(castedArguments.get(0)).and(node.getSelector()).lte(castedArguments.get(1));
        } else {
            return null;
        }
    }
}
