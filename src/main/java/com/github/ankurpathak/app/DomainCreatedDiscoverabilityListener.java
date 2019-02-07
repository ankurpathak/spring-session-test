package com.github.ankurpathak.app;

import com.github.ankurpathak.app.domain.model.Domain;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.net.URI;

@Component
class DomainCreatedDiscoverabilityListener implements ApplicationListener<DomainCreatedEvent<Domain<Serializable>, Serializable>> {

    void addLinkHeaderOnResourceCreation(final HttpServletResponse response, final Serializable id) {
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path(String.format("{%s}", PATH_PARAM_ID)).buildAndExpand(PATH_PARAM_ID).toUri();
        response.setHeader(HttpHeaders.LOCATION, uri.toASCIIString());
    }

    public static final String PATH_PARAM_ID = "id";


    @Override
    public void onApplicationEvent(DomainCreatedEvent<Domain<Serializable>, Serializable> event) {
        final HttpServletResponse response = event.getResponse();
        final Serializable id = event.getId();
        addLinkHeaderOnResourceCreation(response, id);
    }
}