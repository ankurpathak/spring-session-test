package com.ankurpathak.springsessiontest;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

/**
 *
 *
 *
 * Aug 3, 2016
 */
public class WebUtil {
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "X-Requested-With";

    public static boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }



    public static boolean isAjax(SavedRequest request) {
        return request.getHeaderValues(X_REQUESTED_WITH).contains(XML_HTTP_REQUEST);
    }


    public static Locale getLocale(HttpServletRequest request){
        return request.getLocale();
    }


}
