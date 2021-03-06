package com.github.ankurpathak.api.event;

import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;

public final class PaginatedResultsRetrievedEvent extends ExtendedApplicationEvent<Page<?>> {
    private final HttpServletResponse response;
    public PaginatedResultsRetrievedEvent(Page<?> page, final HttpServletResponse response) {
        super(page);
        this.response = response;

    }
    public final HttpServletResponse getResponse() {
        return response;
    }
}
