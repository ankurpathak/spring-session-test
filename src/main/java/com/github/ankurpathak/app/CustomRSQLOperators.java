package com.github.ankurpathak.app;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

import java.util.Set;

public abstract class CustomRSQLOperators extends RSQLOperators {

    public static final ComparisonOperator BETWEEN = new ComparisonOperator(new String[]{"=between="}, true);




    public static Set<ComparisonOperator> defaultOperators() {
        Set<ComparisonOperator> operators = RSQLOperators.defaultOperators();
        operators.add(BETWEEN);
        return operators;
    }

}
