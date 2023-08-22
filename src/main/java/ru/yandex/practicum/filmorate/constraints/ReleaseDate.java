package ru.yandex.practicum.filmorate.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = RealeseDateValidator.class)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
public @interface ReleaseDate {
    String message() default "{ru.yandex.practicum.filmorate.constraints.ReleaseDate.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String after() default "1895-12-19";
}