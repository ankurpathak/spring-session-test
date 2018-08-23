package com.ankurpathak.springsessiontest;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

public class ExtendedApplicationEvent<T> extends ApplicationEvent {
    public ExtendedApplicationEvent(T t) {
        super(t);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getSource() {
        return (T) super.getSource();
    }
}
