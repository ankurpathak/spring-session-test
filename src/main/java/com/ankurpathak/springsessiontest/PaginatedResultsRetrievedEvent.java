package com.ankurpathak.springsessiontest;

import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public final class PaginatedResultsRetrievedEvent<T extends Page<S>, S extends Domain<ID>, ID extends Serializable> extends ExtendedApplicationEvent<T> {
    private final HttpServletResponse response;
    public PaginatedResultsRetrievedEvent(T page, final HttpServletResponse response) {
        super(page);
        this.response = response;

    }
    public final HttpServletResponse getResponse() {
        return response;
    }

}
