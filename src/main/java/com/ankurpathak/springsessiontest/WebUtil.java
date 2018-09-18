package com.ankurpathak.springsessiontest;


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
    private static final String X_REQUESTED_WITH = "X-Requested-With";
    public static final String X_REMEMBER_ME_HEADER = "X-Remember-Me";
    public static final String X_REMEMBER_ME_TOKEN_HEADER = "X-Remember-Me-Token";



    public static boolean isAjax(HttpServletRequest request) {
        //return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
        return true;
    }



    public static boolean isAjax(SavedRequest request) {
        //return request.getHeaderValues(X_REQUESTED_WITH).contains(XML_HTTP_REQUEST);
        return true;
    }


    public static boolean isRememberMeRequested(HttpServletRequest request){
        //return Boolean.parseBoolean(request.getHeader(X_REMEMBER_ME_HEADER));
        return true;
    }


    public static String getRememberMeToken(HttpServletRequest request){
        return request.getHeader(X_REMEMBER_ME_TOKEN_HEADER);
    }

    public static void setRememberMeToken(HttpServletResponse response, String value){
        response.setHeader(X_REMEMBER_ME_TOKEN_HEADER, value);
    }


    public static Locale getLocale(HttpServletRequest request){
        return request.getLocale();
    }


}
