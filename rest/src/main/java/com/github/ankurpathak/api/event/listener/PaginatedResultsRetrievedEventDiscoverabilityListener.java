package com.github.ankurpathak.api.event.listener;

import com.github.ankurpathak.api.event.PaginatedResultsRetrievedEvent;
import com.github.ankurpathak.api.event.util.LinkUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletResponse;

@Component
class PaginatedResultsRetrievedEventDiscoverabilityListener implements ApplicationListener<PaginatedResultsRetrievedEvent> {
    @Override
    public void onApplicationEvent(PaginatedResultsRetrievedEvent ev) {
        addLinkHeaderOnPagedResourceRetrieval(ev.getResponse(), ev.getSource());
        addTotalPageCountHeader(ev.getResponse(), ev.getSource().getTotalElements());
    }


    private void addTotalPageCountHeader(HttpServletResponse response, long totalElement) {
        response.setHeader(HEADER_TOTAL_COUNT, String.valueOf(totalElement));
    }

    private void addLinkHeaderOnPagedResourceRetrieval(final HttpServletResponse response, Page<?> page) {
        final StringBuilder linkHeader = new StringBuilder();
        if (hasNextPage(page)) {
            final String uriForNextPage = constructNextPageUri(page);
            linkHeader.append(LinkUtil.createLinkHeader(uriForNextPage, LinkUtil.REL_NEXT));
        }
        if (hasPreviousPage(page)) {
            final String uriForPrevPage = constructPrevPageUri(page);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(LinkUtil.createLinkHeader(uriForPrevPage, LinkUtil.REL_PREV));
        }
        if (hasFirstPage(page)) {
            final String uriForFirstPage = constructFirstPageUri(page);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(LinkUtil.createLinkHeader(uriForFirstPage, LinkUtil.REL_FIRST));
        }
        if (hasLastPage(page)) {
            final String uriForLastPage = constructLastPageUri(page);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(LinkUtil.createLinkHeader(uriForLastPage, LinkUtil.REL_LAST));
        }
        response.addHeader(HttpHeaders.LINK, linkHeader.toString());
    }

    String constructNextPageUri(final Page<?> page)  {
        return ServletUriComponentsBuilder.fromCurrentRequestUri().replaceQueryParam(QUERY_PARAM_PAGE, page.getNumber() + 1).replaceQueryParam(QUERY_PARAM_SIZE, page.getSize()).build().encode().toUriString();
    }

    String constructPrevPageUri(final Page<?> page) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri().replaceQueryParam(QUERY_PARAM_PAGE, page.getNumber() - 1).replaceQueryParam(QUERY_PARAM_SIZE, page.getSize()).build().encode().toUriString();
    }

    String constructFirstPageUri(final Page<?> page) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri().replaceQueryParam(QUERY_PARAM_PAGE, 0).replaceQueryParam(QUERY_PARAM_SIZE, page.getSize()).build().encode().toUriString();
    }

    String constructLastPageUri(final Page<?> page) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri().replaceQueryParam(QUERY_PARAM_PAGE, page.getTotalPages()).replaceQueryParam(QUERY_PARAM_SIZE, page.getSize()).build().encode().toUriString();
    }

    boolean hasNextPage(Page<?> page) {
        //return page.getNumber() < page.getTotalPages() - 1;
        return page.hasNext();
    }

    boolean hasPreviousPage(Page<?> page) {
       // return page.getNumber() > 0;
        return page.hasPrevious();
    }

    boolean hasFirstPage(Page<?> page) {
        return hasPreviousPage(page);
    }

    boolean hasLastPage(Page<?> page) {
        return page.getTotalPages() > 1 && hasNextPage(page);
    }

    void appendCommaIfNecessary(final StringBuilder linkHeader) {
        if (linkHeader.length() > 0) {
            linkHeader.append(", ");
        }
    }

    public static String QUERY_PARAM_SIZE = "size";
    public static String QUERY_PARAM_PAGE = "page";
    public static String HEADER_TOTAL_COUNT = "X-Total-Count";


}