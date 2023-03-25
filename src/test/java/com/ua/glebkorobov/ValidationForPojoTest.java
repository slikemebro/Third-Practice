package com.ua.glebkorobov;

import com.ua.glebkorobov.dto.Pojo;
import com.ua.glebkorobov.exceptions.CsvWriteException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidationForPojoTest {

    private static final GetProperty property = new GetProperty("myProp.properties");


    @Test
    void validTest() {
        Pojo pojoTest1 = new Pojo("Gleb", 10, LocalDateTime.now());//2
        Pojo pojoTest2 = new Pojo("alex", 10, LocalDateTime.now());//1
        Pojo pojoTest3 = new Pojo("lexsdqw", 10, LocalDateTime.now());//1
        Pojo pojoTest4 = new Pojo("woDR", 1, LocalDateTime.now());//3

        Pojo pojoTest5 = new Pojo("TYMxAfateDVV", 10, LocalDateTime.now());

        ValidationForPojo validation = new ValidationForPojo();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Pojo>> violations = validator.validate(pojoTest1);
        assertEquals(2, validation.valid(pojoTest1).size());

        violations = validator.validate(pojoTest2);
        assertEquals(1, validation.valid(pojoTest2).size());

        violations = validator.validate(pojoTest3);
        assertEquals(1, validation.valid(pojoTest3).size());

        violations = validator.validate(pojoTest4);
        assertEquals(3, validation.valid(pojoTest4).size());


        assertEquals(0, validation.valid(pojoTest5).size());

    }

    @Test
    void createMapperTest() {
        ValidationForPojo validation = new ValidationForPojo();
        assertNotNull(validation.createMapper());
    }

    @Test
    void createValidCsvWriterTest() {
        ValidationForPojo validation = new ValidationForPojo();
        assertNotNull(validation.createValidCsvWriter());
    }

    @Test
    void createValidCsvWriterExceptionTest() {
        ValidationForPojo validation = new ValidationForPojo();
        validation.validPojoWriterFile = null;
        assertThrows(CsvWriteException.class, validation::createValidCsvWriter);
    }

    @Test
    void createInvalidCsvWriterTest() {
        ValidationForPojo validation = new ValidationForPojo();
        assertNotNull(validation.createInValidCsvWriter());
    }

    @Test
    void createInvalidCsvWriterExceptionTest() {
        ValidationForPojo validation = new ValidationForPojo();
        validation.invalidPojoWriterFile = null;
        assertThrows(CsvWriteException.class, validation::createInValidCsvWriter);
    }

    @Test
    void createValidatorTest() {
        ValidationForPojo validation = new ValidationForPojo();
        assertNotNull(validation.createValidator());
    }

    @Test
    void createErrorsTest() {
        ValidationForPojo validation = new ValidationForPojo();

        Pojo test = new Pojo("Gleb", 123, LocalDateTime.now());
        Pojo test2 = new Pojo("Glebaaaaaaaaaaaa", 123, LocalDateTime.now());

        Validator validator = validation.createValidator();

        Set<ConstraintViolation<Pojo>> violations = validator.validate(test);

        assertEquals(2, validation.createErrors(violations).length);

        violations = validator.validate(test2);

        assertEquals(0, validation.createErrors(violations).length);

    }
}