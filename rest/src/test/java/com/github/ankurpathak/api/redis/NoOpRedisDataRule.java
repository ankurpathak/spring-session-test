package com.github.ankurpathak.api.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankurpathak.api.AbstractRestIntegrationTest;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;
import static org.valid4j.Assertive.require;

public class NoOpRedisDataRule<SELF extends AbstractRestIntegrationTest<SELF>> implements AfterEachCallback, BeforeEachCallback, TestRule {
    private AbstractRestIntegrationTest<SELF> test;





    public NoOpRedisDataRule(AbstractRestIntegrationTest<SELF> test) {
        require(test, notNullValue());
        this.test = test;
    }











    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        after();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        before();
    }

    public void before()throws Exception{
    }

    public void after() throws Exception{

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
}
