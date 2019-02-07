package com.github.ankurpathak.app.annotation;

import com.github.ankurpathak.app.Params;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal(expression = Params.USER)
public @interface CurrentUser {
}
