package com.github.ankurpathak.api.util;


import com.github.ankurpathak.api.constant.Params;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.Locale;
import java.util.Optional;

/**
 *
 *
 *
 * Aug 3, 2016
 */
public class WebUtil {

    public static boolean isAjax(HttpServletRequest request) {
        return Params.Header.XML_HTTP_REQUEST.equals(request.getHeader(Params.Header.X_REQUESTED_WITH));
    }



    public static boolean isAjax(SavedRequest request) {
        return request.getHeaderValues(Params.Header.X_REQUESTED_WITH).contains(Params.Header.XML_HTTP_REQUEST);
    }


    public static boolean isRememberMeRequested(HttpServletRequest request){
        return Boolean.parseBoolean(request.getHeader(Params.Header.X_REMEMBER_ME));
    }

    public static boolean isOtpFlow(HttpServletRequest request){
        return Boolean.parseBoolean(request.getHeader(Params.Header.X_OTP_FLOW));
    }

    public static boolean isAsync(HttpServletRequest request){
        return Boolean.parseBoolean(request.getParameter(Params.Query.ASYNC));
    }


    public static String getRememberMeToken(HttpServletRequest request){
        return request.getHeader(Params.Header.X_REMEMBER_ME_TOKEN);
    }

    public static void setRememberMeToken(HttpServletResponse response, String value){
        response.setHeader(Params.Header.X_REMEMBER_ME_TOKEN, value);
    }

    public static BigInteger getRequestedBusinessId(HttpServletRequest request){
        return PrimitiveUtils.toBigInteger(request.getHeader(Params.Header.X_REQUESTED_MY_ORGANIZATION));
    }


    public static Locale getLocale(HttpServletRequest request){
        return request.getLocale();
    }


    public static Optional<HttpServletRequest> getRequest(){
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(attr -> ServletRequestAttributes.class.isAssignableFrom(attr.getClass()))
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }


    public static Optional<HttpServletResponse> getResponse(){
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(attr -> ServletRequestAttributes.class.isAssignableFrom(attr.getClass()))
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getResponse);
    }


    public static Locale getLocale(){
        return LocaleContextHolder.getLocale();
    }


}
