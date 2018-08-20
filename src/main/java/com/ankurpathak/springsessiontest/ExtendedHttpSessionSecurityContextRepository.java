package com.ankurpathak.springsessiontest;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExtendedHttpSessionSecurityContextRepository extends HttpSessionSecurityContextRepository {

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        SecurityContext context =  super.loadContext(requestResponseHolder);
        if(context instanceof SecurityContextImpl){
            DomainContext domainContext = new DomainContext(requestResponseHolder.getRequest());
            SecurityContextCompositeImpl contextExtended = new SecurityContextCompositeImpl(context, domainContext);
            return contextExtended;
        }else {
            return context;
        }
    }

    @Override
    public void saveContext(SecurityContext contextIn, HttpServletRequest request, HttpServletResponse response) {
        if(contextIn instanceof SecurityContextCompositeImpl){
            SecurityContext context = ((SecurityContextCompositeImpl)contextIn).getSecurityContext();
            super.saveContext(context, request, response);
        }else {
            super.saveContext(contextIn, request, response);

        }
    }
}
