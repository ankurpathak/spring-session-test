package com.ankurpathak.springsessiontest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class DomainContext extends WebAuthenticationDetails implements Serializable {


    public DomainContext(final HttpServletRequest request) {
        super(request);

    }

}
