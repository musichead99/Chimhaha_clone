package net.chimhaha.clone.config.annotation;

import net.chimhaha.clone.config.annotation.validator.MultipartExistValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultipartExistValidator.class)
public @interface MultipartExist {
    String message() default "MultipartFile 필드가 비어있습니다.";
    Class[] groups() default {};
    Class[] payload() default {};
}
