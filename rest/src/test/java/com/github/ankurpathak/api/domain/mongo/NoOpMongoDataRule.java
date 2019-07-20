package com.github.ankurpathak.api.domain.mongo;

import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class NoOpMongoDataRule<SELF extends AbstractRestIntegrationTest<SELF>> implements AfterEachCallback, BeforeEachCallback, TestRule {
    private AbstractRestIntegrationTest<SELF> test;
    public void before()throws Exception{ }

    public void after() throws Exception{ }


    public NoOpMongoDataRule(AbstractRestIntegrationTest<SELF> test) {
        require(test, notNullValue());
        this.test = test;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before();
                try {
                    statement.evaluate();
                }finally {
                    after();
                }
            }
        };
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        before();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        after();
    }
}
