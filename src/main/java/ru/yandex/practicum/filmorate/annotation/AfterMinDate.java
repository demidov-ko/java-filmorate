package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AfterMinDateValidator.class)
public @interface AfterMinDate {
    String message() default "Дата релиза — не раньше 28 декабря 1895 года";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
