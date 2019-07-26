package com.github.ankurpathak.api.constraint;

import com.github.ankurpathak.api.constraint.validator.VariableOrAmountValidator;
import com.github.ankurpathak.password.bean.constraints.validator.PasswordMatchesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {VariableOrAmountValidator.class}
)
@Documented
@Repeatable(VariableOrAmount.List.class)
public @interface VariableOrAmount {

    String message() default "{com.github.ankurpathak.api.constraint.VariableOrAmount.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean ignoreBlankVariable() default true;

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        VariableOrAmount[] value();
    }
}

