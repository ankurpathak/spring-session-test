package com.github.ankurpathak.app;

import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class GetHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public GetHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }


    @Override
    public String getMethod() {
        return HttpMethod.GET.name();
    }
}
