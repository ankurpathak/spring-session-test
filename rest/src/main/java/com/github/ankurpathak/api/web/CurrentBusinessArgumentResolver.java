package com.github.ankurpathak.api.web;

import com.github.ankurpathak.api.annotation.CurrentBusiness;
import com.github.ankurpathak.api.constant.Model;
import com.github.ankurpathak.api.constant.Params;
import com.github.ankurpathak.api.domain.model.Business;
import com.github.ankurpathak.api.exception.NotFoundException;
import com.github.ankurpathak.api.rest.controller.dto.ApiCode;
import com.github.ankurpathak.api.security.dto.DomainContext;
import com.github.ankurpathak.api.security.dto.DomainContextHolder;
import com.github.ankurpathak.api.security.util.SecurityUtil;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.math.BigInteger;
import java.util.Optional;

public class CurrentBusinessArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return findMethodAnnotation(CurrentBusiness.class, parameter) != null;
    }


    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer container, NativeWebRequest request,
                                  WebDataBinderFactory factory) throws Exception {
        CurrentBusiness currBusiness = findMethodAnnotation(
                CurrentBusiness.class, parameter);
        Optional<Business> business = SecurityUtil.getRequestedMyBusiness();
        if (business.isPresent()){
            return business.get();
        }else {
            if(currBusiness.errorOnNotFound()){
                throw new NotFoundException(String.valueOf(SecurityUtil.getRequestedBusinessId().orElse(BigInteger.ZERO)), Params.ID, "Business",ApiCode.BUSINESS_NOT_FOUND );
            }else {
                return null;
            }
        }
    }



    private <T extends Annotation> T findMethodAnnotation(Class<T> annotationClass,
                                                          MethodParameter parameter) {
        T annotation = parameter.getParameterAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }
        Annotation[] annotationsToSearch = parameter.getParameterAnnotations();
        for (Annotation toSearch : annotationsToSearch) {
            annotation = AnnotationUtils.findAnnotation(toSearch.annotationType(),
                    annotationClass);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

}
