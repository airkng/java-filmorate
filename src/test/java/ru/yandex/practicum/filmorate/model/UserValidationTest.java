package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserValidationTest {
    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }


    @Test
    void shouldReturnZero_AllParamsIsGood() {
        User user = new User(1, "example@email.com", "dev", "example", LocalDate.of(2000, 12, 1),
                "test");
        user.setName(" ");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size(), "All is ok");
    }
    @Test
    void shouldReturnOneException_EmailException() {
        User user = new User(1, "incorrectEmail", "lol", " ", LocalDate.of(2000, 12, 1),
                "test");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }
    @Test
    void shouldReturnTwoExceptions_EmailAndLogin() {
        User user = new User(1, "incorrectEmail", " ", " ", LocalDate.of(2000, 12, 1),
                "Неподтвержденная");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(2, violations.size());
    }

    @Test
    void shouldReturnTwoExceptions_LoginAndDate() {
        User user = new User(1, "correctEmail@email.com", " ", " ", LocalDate.of(2024, 12, 1),
                "Подтвержденная");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(2, violations.size());
    }
}
