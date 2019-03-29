package com.github.ankurpathak.app.security.core;

import org.springframework.security.web.DefaultRedirectStrategy;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatcherRedirectStrategy extends DefaultRedirectStrategy {


    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {

        String redirectUrl = calculateRedirectUrl(request.getContextPath(), url);
        redirectUrl = response.encodeRedirectURL(redirectUrl);

        if (logger.isDebugEnabled()) {
            logger.debug("Redirecting to '" + redirectUrl + "'");
        }
        HttpServletRequest getRequest = new GetHttpServletRequestWrapper(request);
        RequestDispatcher dispatcher = getRequest.getRequestDispatcher(redirectUrl);

        try {
            dispatcher.include(getRequest, response);
        } catch (ServletException ex) {
            ex.printStackTrace();
            throw new IOException(ex.getMessage(), ex);
        }
    }
}
