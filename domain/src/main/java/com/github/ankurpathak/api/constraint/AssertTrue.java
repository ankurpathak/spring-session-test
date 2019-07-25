package com.github.ankurpathak.api.constraint;

import com.github.ankurpathak.api.constraint.validator.AssertTrueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AssertTrue.List.class)
@Documented
@Constraint(
        validatedBy = {AssertTrueValidator.class}
)
public @interface AssertTrue {
    String message() default "{com.github.ankurpathak.api.constraint.AssertTrue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean ignoreBlank() default true;


    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        AssertTrue[] value();
    }
}
