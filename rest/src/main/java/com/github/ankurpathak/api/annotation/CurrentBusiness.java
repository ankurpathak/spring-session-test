package com.github.ankurpathak.api.annotation;

import com.github.ankurpathak.api.constant.Params;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentBusiness {
    boolean errorOnNotFound() default true;
}
