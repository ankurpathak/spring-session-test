package com.github.ankurpathak.api.domain.repository.dto;

import static org.hamcrest.Matchers.notNullValue;
import static org.valid4j.Assertive.require;

public class MessageContext {
    private final Object message;
    private final String target;
    private final String key;

    public MessageContext(Object message, String target, String key) {
        require(message, notNullValue());
        require(target, notNullValue());
        require(key, notNullValue());
        this.message = message;
        this.target = target;
        this.key = key;

    }

    public String getKey() {
        return key;
    }

    public Object getMessage() {
        return message;
    }

    public String getTarget() {
        return target;
    }
}
