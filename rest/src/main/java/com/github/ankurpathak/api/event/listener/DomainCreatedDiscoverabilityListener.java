package com.github.ankurpathak.api.event.listener;

import com.github.ankurpathak.api.domain.model.Domain;
import com.github.ankurpathak.api.event.DomainCreatedEvent;
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
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(id).toUri();
        response.addHeader(HttpHeaders.LOCATION, uri.toASCIIString());
    }

    @Override
    public void onApplicationEvent(DomainCreatedEvent<Domain<Serializable>, Serializable> event) {
        final HttpServletResponse response = event.getResponse();
        final Serializable id = event.getId();
        addLinkHeaderOnResourceCreation(response, id);
    }
}