package net.chimhaha.clone.annotation;

import net.chimhaha.clone.annotation.validator.MultipartExistValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultipartExistValidator.class)
public @interface MultipartExist {
    String message() default "MultipartFile is Empty";
    Class[] groups() default {};
    Class[] payload() default {};
}
