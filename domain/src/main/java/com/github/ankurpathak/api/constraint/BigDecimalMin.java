package com.github.ankurpathak.api.constraint;

import com.github.ankurpathak.api.constraint.validator.BigDecimalMinValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(BigDecimalMin.List.class)
@Documented
@Constraint(
        validatedBy = {BigDecimalMinValidator.class}
)
public @interface BigDecimalMin {
    String message() default "{com.github.ankurpathak.api.constraint.BigDecimalMin.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();

    boolean inclusive() default true;

    boolean ignoreBlank() default true;

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        BigDecimalMin[] value();
    }
}
