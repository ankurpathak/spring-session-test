package com.github.ankurpathak.api.security.dto;

import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.util.WebUtil;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Locale;

public class DomainContext extends WebAuthenticationDetails implements Serializable {

    private final boolean isOtpFlow;
    private final boolean isAsync;
    private final BigInteger requestedBusinessId;
    private Business business;

    public DomainContext(final HttpServletRequest request) {
        super(request);
        this.isOtpFlow = WebUtil.isOtpFlow(request);
        this.isAsync = WebUtil.isAsync(request);
        this.requestedBusinessId = WebUtil.getRequestedBusinessId(request);
    }


    public BigInteger getRequestedBusinessId() {
        return requestedBusinessId;
    }

    public boolean isAsync() { return this.isAsync; }


    public boolean isOtpFlow() {
        return isOtpFlow;
    }


    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
