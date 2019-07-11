package com.github.ankurpathak.api.util;


import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 *
 *
 *
 * Aug 3, 2016
 */
public class WebUtil {
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String HEADER_X_REQUESTED_WITH = "X-Requested-With";
    public static final String HEADER_X_REMEMBER_ME_TOKEN = "X-Remember-Me-Token";
    public static final String HEADER_X_REMEMBER_ME = "X-Remember-Me";
    public static final String HEADER_X_AUTH_TOKEN = "X-Auth-Token";
    public static final String COO = "X-Auth-Token";





    public static boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(HEADER_X_REQUESTED_WITH));
    }



    public static boolean isAjax(SavedRequest request) {
        return request.getHeaderValues(HEADER_X_REQUESTED_WITH).contains(XML_HTTP_REQUEST);
    }


    public static boolean isRememberMeRequested(HttpServletRequest request){
        return Boolean.parseBoolean(request.getHeader(HEADER_X_REMEMBER_ME));
    }


    public static String getRememberMeToken(HttpServletRequest request){
        return request.getHeader(HEADER_X_REMEMBER_ME_TOKEN);
    }

    public static void setRememberMeToken(HttpServletResponse response, String value){
        response.setHeader(HEADER_X_REMEMBER_ME_TOKEN, value);
    }


    public static Locale getLocale(HttpServletRequest request){
        return request.getLocale();
    }


}
