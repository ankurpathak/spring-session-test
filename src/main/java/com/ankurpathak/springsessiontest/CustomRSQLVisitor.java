package com.ankurpathak.springsessiontest;


import cz.jirutka.rsql.parser.ast.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.stream.Collectors;

public class CustomRSQLVisitor extends AbstractRSQLNodeVisitor<Criteria>{

    private String resource;

    public CustomRSQLVisitor(String resource) {
        this.resource = resource;
    }

    @Override
    public Criteria visit(final AndNode node) {
        Criteria criteria = new Criteria();
        List<Criteria> children = node.getChildren().stream().map(this::visitAny).collect(Collectors.toList());
        return criteria.andOperator((Criteria[])children.toArray(new Criteria[children.size()]));

    }

    @Override
    public Criteria visit(final OrNode node) {
        Criteria criteria = new Criteria();
        List<Criteria> children = node.getChildren().stream().map(this::visitAny).collect(Collectors.toList());
        return criteria.orOperator((Criteria[])children.toArray(new Criteria[children.size()]));
    }

    @Override
    public Criteria visit(final ComparisonNode node) {
        return CriteriaFactory.create(node, resource);
    }







}