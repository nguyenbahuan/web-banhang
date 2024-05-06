package com.spring.electronicshop.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = CapitalizedValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CapitalizedConstraint {
	String message() default "Chữ đầu tiên phải được in hoa!";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
