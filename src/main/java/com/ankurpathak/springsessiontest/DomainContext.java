package com.ankurpathak.springsessiontest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class DomainContext extends WebAuthenticationDetails {


    public DomainContext(final HttpServletRequest request) {
        super(request);

    }

}
