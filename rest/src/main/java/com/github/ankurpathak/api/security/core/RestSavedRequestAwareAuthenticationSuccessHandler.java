package com.github.ankurpathak.api.security.core;

import com.github.ankurpathak.api.service.IFilterService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class RestSavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String DEFAULT_TARGET = "/api/me";
    public static final String DEFAULT_TARGET_PARAMETER = "defaultTarget";
    private RequestCache requestCache = new HttpSessionRequestCache();


    private final IFilterService filterService;
    public RestSavedRequestAwareAuthenticationSuccessHandler(IFilterService filterService) {
        setRedirectStrategy(new DispatcherRedirectStrategy());
        setAlwaysUseDefaultTargetUrl(true);
        setDefaultTargetUrl(DEFAULT_TARGET);
        setTargetUrlParameter(DEFAULT_TARGET_PARAMETER);
        this.filterService = filterService;

    }


    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws ServletException, IOException {
        final SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
           // handle(request, response, authentication); //added for redirect
            clearAuthenticationAttributes(request);
            generateResponse(request, response);  // for no redirect
            return;
        }
        final String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
          //  handle(request, response, authentication); //added for redirect
            clearAuthenticationAttributes(request);
            generateResponse(request, response); // for no redirect
            return;
        }

        clearAuthenticationAttributes(request);
        generateResponse(request, response); // for no redirect



        // Use the DefaultSavedRequest URL
       /* String targetUrl = getDefaultTargetUrl();
        if(Objects.equals(savedRequest.getMethod(), HttpMethod.GET.name()))
            targetUrl = savedRequest.getRedirectUrl();
        logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl); */


    }

    public void setRequestCache(final RequestCache requestCache) {
        this.requestCache = requestCache;
    }


    private void generateResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        filterService.generateSuccess(response);
    }


}
