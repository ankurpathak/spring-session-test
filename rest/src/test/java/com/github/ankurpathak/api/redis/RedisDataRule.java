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
import java.util.*;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.ensure;
import static org.valid4j.Assertive.require;

public class RedisDataRule<SELF extends AbstractRestIntegrationTest<SELF>> implements AfterEachCallback, BeforeEachCallback, TestRule {
    private AbstractRestIntegrationTest<SELF> test;
    private RedisTemplate<?, ?> template;
    private ObjectMapper objectMapper;
    private Map<String, Resource> jsons = null;
    private RedisConnectionFactory connectionFactory;




    public RedisTemplate<?, ?> getTemplate() {
        if(this.template == null){
            this.template = setUpTemplate();
            ensure(template, notNullValue());
        }
        return this.template;
    }

    public ObjectMapper getObjectMapper() {
        if(this.objectMapper == null){
            this.objectMapper = setUpObjectMapper();
            ensure(this.objectMapper, notNullValue());
        }
        return this.objectMapper;
    }


    public RedisConnectionFactory getConnectionFactory() {
        if(this.connectionFactory == null){
            this.connectionFactory = setUpConnectionFactory();
            ensure(this.connectionFactory, notNullValue());
        }
        return this.connectionFactory;
    }





    public RedisDataRule(AbstractRestIntegrationTest<SELF> test) {
        require(test, notNullValue());
        this.test = test;
    }



    private RedisTemplate<?, ?> setUpTemplate(){
        Field field = ReflectionUtils.findField(test.getClass(), "redisTemplate");
        if(field != null) {
            field.setAccessible(true);
            Object fileldValue = ReflectionUtils.getField(field, test);
            if(fileldValue instanceof RedisTemplate)
                return (RedisTemplate) fileldValue;
        }
        return null;
    }


    private RedisConnectionFactory setUpConnectionFactory(){
        this.connectionFactory = this.getTemplate().getConnectionFactory();
        return this.connectionFactory;
    }

    private ObjectMapper setUpObjectMapper(){
        Field field = ReflectionUtils.findField(test.getClass(), "objectMapper");
        if(field != null) {
            field.setAccessible(true);
            Object fileldValue = ReflectionUtils.getField(field, test);
            if(fileldValue instanceof ObjectMapper)
                return (ObjectMapper) fileldValue;
        }
        return null;
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
        getConnectionFactory().getConnection().flushAll();
    }

    public void after() throws Exception{

    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RedisDataRule.this.before();
                try {
                    statement.evaluate();
                }finally {
                    RedisDataRule.this.after();
                }
            }
        };
    }
}
