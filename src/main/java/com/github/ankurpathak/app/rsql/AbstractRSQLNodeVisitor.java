package com.github.ankurpathak.app.rsql;

import cz.jirutka.rsql.parser.ast.*;

public abstract class AbstractRSQLNodeVisitor<T> implements RSQLVisitor<T, Void> {
    @Override
    public T visit(AndNode andNode, Void param) {
        return visit(andNode);
    }
    public abstract T visit(AndNode andNode);




    @Override
    public T visit(OrNode orNode, Void param) {
        return visit(orNode);
    }

    public abstract T visit(OrNode orNode);

    @Override
    public T visit(ComparisonNode comparisonNode, Void aVoid) {
        return visit(comparisonNode);
    }

    public abstract T visit(ComparisonNode comparisonNode);


    public final T visitAny(Node node) {
        if (node instanceof LogicalNode) {
            LogicalNode logical = (LogicalNode)node;
            if (logical.getChildren().size() == 1) {
                return this.visitAny(logical.getChildren().get(0));
            }
        }

        if (node instanceof AndNode) {
            return this.visit((AndNode)node);
        } else {
            return node instanceof OrNode ? this.visit((OrNode)node) : this.visit((ComparisonNode)node);
        }
    }

}
