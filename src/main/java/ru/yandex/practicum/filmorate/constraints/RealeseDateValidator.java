package ru.yandex.practicum.filmorate.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class RealeseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {
    private LocalDate realeseDate;

    @Override
    public void initialize(final ReleaseDate constraintAnnotation) {
        realeseDate = LocalDate.parse(constraintAnnotation.after());
    }

    @Override
    public boolean isValid(final LocalDate localDate, final ConstraintValidatorContext constraintValidatorContext) {
        try {
            return realeseDate == null && localDate == null || localDate.isAfter(realeseDate) && realeseDate != null;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}