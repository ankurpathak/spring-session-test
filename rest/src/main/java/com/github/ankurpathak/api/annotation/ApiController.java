package com.github.ankurpathak.api.annotation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.github.ankurpathak.api.constant.ApiPaths.PATH_API;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(PATH_API)
@RestController
public @interface ApiController {
}




