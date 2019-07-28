package com.github.ankurpathak.api.constraint;

import com.github.ankurpathak.api.constraint.validator.VariableOrAmountValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {VariableOrAmountValidator.class}
)
@Documented
@Repeatable(Address.List.class)
public @interface Address {

    String message() default "{com.github.ankurpathak.api.constraint.Address.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean ignoreBlankAddress() default true;

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Address[] value();
    }
}

