package com.github.ankurpathak.app;

import org.springframework.context.ApplicationEvent;

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
