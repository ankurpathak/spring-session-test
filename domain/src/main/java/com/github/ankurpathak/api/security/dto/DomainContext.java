package com.github.ankurpathak.api.security.dto;

import com.github.ankurpathak.api.util.WebUtil;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Locale;

public class DomainContext extends WebAuthenticationDetails implements Serializable {

    private final Locale locale;
    private final boolean isOtpFlow;
    private final boolean isAsync;

    public DomainContext(final HttpServletRequest request) {
        super(request);
        this.locale = WebUtil.getLocale(request);
        this.isOtpFlow = WebUtil.isOtpFlow(request);
        this.isAsync = WebUtil.isAsync(request);
    }

    public Locale getLocale() {
        return this.locale;
    }
    public boolean getOtpFlow() { return this.isOtpFlow; }
    public boolean isAsync() { return this.isAsync; }
}
