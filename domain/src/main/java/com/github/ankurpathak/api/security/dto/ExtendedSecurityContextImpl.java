package com.github.ankurpathak.api.security.dto;

import com.github.ankurpathak.api.security.dto.DomainContext;
import org.springframework.security.core.context.SecurityContextImpl;

public class ExtendedSecurityContextImpl extends SecurityContextImpl {
    private DomainContext domainContext;


    public DomainContext getDomainContext() {
        return domainContext;
    }

    public void setDomainContext(DomainContext domainContext) {
        this.domainContext = domainContext;
    }
}
