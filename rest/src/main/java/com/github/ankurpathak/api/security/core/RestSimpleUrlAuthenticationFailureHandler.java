package com.github.ankurpathak.api.security.core;

import com.github.ankurpathak.api.service.IFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class RestSimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler {
    protected static final Logger logger = LoggerFactory.getLogger(RestSimpleUrlAuthenticationFailureHandler.class);

    private String defaultFailureUrl;
    private boolean forwardToDestination = false;
    private boolean allowSessionCreation = true;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final IFilterService filterService;

    @Autowired
    public RestSimpleUrlAuthenticationFailureHandler(IFilterService filterService) {
        this.filterService = filterService;
    }

    public RestSimpleUrlAuthenticationFailureHandler(String defaultFailureUrl, IFilterService filterService) {
        this.filterService = filterService;
        setDefaultFailureUrl(defaultFailureUrl);
    }
  public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException ex)
            throws IOException, ServletException {

        if (defaultFailureUrl == null) {
            logger.error("message: {} cause: {} path: {}", ex.getMessage(), ex.getCause(), request.getRequestURI());
            ex.printStackTrace();
            generateResponse(request,response, ex);
        }
        else {
            saveException(request, ex);

            if (forwardToDestination) {
                logger.debug("Forwarding to " + defaultFailureUrl);

                request.getRequestDispatcher(defaultFailureUrl)
                        .forward(request, response);
            }
            else {
                logger.debug("Redirecting to " + defaultFailureUrl);
                redirectStrategy.sendRedirect(request, response, defaultFailureUrl);
            }
        }
    }
    protected final void saveException(HttpServletRequest request,
                                       AuthenticationException exception) {
        if (forwardToDestination) {
            request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
        }
        else {
            HttpSession session = request.getSession(false);

            if (session != null || allowSessionCreation) {
                request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
                        exception);
            }
        }
    }

    public void setDefaultFailureUrl(String defaultFailureUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl), "'"
                + defaultFailureUrl + "' is not a valid redirect URL");
        this.defaultFailureUrl = defaultFailureUrl;
    }

    protected boolean isUseForward() {
        return forwardToDestination;
    }

    public void setUseForward(boolean forwardToDestination) {
        this.forwardToDestination = forwardToDestination;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    protected boolean isAllowSessionCreation() {
        return allowSessionCreation;
    }

    public void setAllowSessionCreation(boolean allowSessionCreation) {
        this.allowSessionCreation = allowSessionCreation;
    }

    private void generateResponse(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        filterService.generateUnauthorized(response, ex);
    }
}
