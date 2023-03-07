package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmValidationTest {
    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void shouldReturnZero_AllParamsIsGood() {
       Film film = new Film(1, "name", "description", LocalDate.of(2020, 4, 17),100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size(), "All is ok");
    }
    @Test
    void shouldReturnOneException_NameException() {
        Film film = new Film(1, " ", "description", LocalDate.of(2020, 4, 17),100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }
    @Test
    void shouldReturnTwoExceptions_NameAndDescription() {
        Film film = new Film(1
                , ""
                , "description jfdninvodfivdfo dn foinvifndvodin dofinovndofinvndifnv voidfoivnfdiofdnvijfndvidfnvodinf" +
                "vfmdovpodfmvpodfmvpodfmopvmdfpovmpdfom ivnodifnvodifnvidfonv oifnviodvidnfvoindfoivndfoivsdNClkjn" +
                "v dfiovfidovnoidfnv oivdfmviodfmvoidfmvmdofimvdfvopijv oivvoidfvndfoivndfoinvdfo ovidnvoidnfvoidfnvoidnfvoidn"
                , LocalDate.of(2020, 4, 17),100);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(2, violations.size());
    }

   @Test
    void shouldReturnOneException_Duration() {
        Film film = new Film(1, "dd", "description", LocalDate.of(2020, 4, 17),-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }
}
