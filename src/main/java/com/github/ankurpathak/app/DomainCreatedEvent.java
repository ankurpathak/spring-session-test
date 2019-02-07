package com.github.ankurpathak.app;

import com.github.ankurpathak.app.domain.model.Domain;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public class DomainCreatedEvent<T extends Domain<ID>, ID extends Serializable> extends ExtendedApplicationEvent<T> {
    private final HttpServletResponse response;

    public DomainCreatedEvent(final T source, final HttpServletResponse response) {
        super(source);
        this.response = response;
    }
    public HttpServletResponse getResponse() {
        return response;
    }


    public ID getId() {
        return getSource().getId();
    }
}
