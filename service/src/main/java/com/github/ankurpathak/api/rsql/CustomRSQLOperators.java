package com.github.ankurpathak.api.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

import java.util.List;
import java.util.Set;

public abstract class CustomRSQLOperators extends RSQLOperators {

    public static final ComparisonOperator BETWEEN = new ComparisonOperator(new String[]{"=between="}, true);
    public static final ComparisonOperator STARTING_WITH = new ComparisonOperator(new String[]{"=s="}, false);
    public static final ComparisonOperator ENDING_WITH = new ComparisonOperator(new String[]{"=e="}, false);
    public static final ComparisonOperator CONTANING = new ComparisonOperator(new String[]{"=c="}, false);




    public static Set<ComparisonOperator> defaultOperators() {
        Set<ComparisonOperator> operators = RSQLOperators.defaultOperators();
        operators.addAll(List.of(BETWEEN,STARTING_WITH,ENDING_WITH,CONTANING));
        return operators;
    }

}
