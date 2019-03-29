package com.github.ankurpathak.app.security.dto;

import com.github.ankurpathak.app.util.WebUtil;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Locale;

public class DomainContext extends WebAuthenticationDetails implements Serializable {

    private Locale locale;

    public DomainContext(final HttpServletRequest request) {
        super(request);
        locale = WebUtil.getLocale(request);
    }

    public Locale getLocale() {
        return locale;
    }

}