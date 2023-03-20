package com.ua.glebkorobov;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.ua.glebkorobov.dto.Pojo;
import com.ua.glebkorobov.exceptions.CloseWriterException;
import com.ua.glebkorobov.exceptions.CsvWriteException;
import com.ua.glebkorobov.exceptions.GetFieldException;
import com.ua.glebkorobov.exceptions.WriterCreateException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

public class ValidationForList {

    private ValidationForList() {
        throw new IllegalStateException("Utility class");
    }

    public static void valid(List<Pojo> receivedList) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Writer writerForCorrect;
        Writer writerForWrong;

        try {
            writerForCorrect = new FileWriter("testFile.csv");
            writerForWrong = new FileWriter("testFile2.csv");
        } catch (IOException e) {
            throw new WriterCreateException(e);
        }


        StatefulBeanToCsv<Pojo> correctBeanToCsv;
        try {
            correctBeanToCsv = new StatefulBeanToCsvBuilder<Pojo>(writerForCorrect)
                    .withApplyQuotesToAll(true)
                    .withIgnoreField(Pojo.class, Pojo.class.getDeclaredField("errors"))
                    .build();
        } catch (NoSuchFieldException e) {
            throw new GetFieldException(e);
        }

        StatefulBeanToCsv<Pojo> wrongBeanToCsv = new StatefulBeanToCsvBuilder<Pojo>(writerForWrong)
                .withApplyQuotesToAll(true)
                .build();

        try {
            for (Pojo pojo : receivedList) {
                Set<ConstraintViolation<Pojo>> violations = validator.validate(pojo);
                if (violations.isEmpty()) {
                    correctBeanToCsv.write(pojo);
                } else {
                    pojo.setErrors(violations);
                    wrongBeanToCsv.write(pojo);
                }
            }
        }catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            throw new CsvWriteException(e);
        }


        try {
            writerForCorrect.close();
            writerForWrong.close();
        } catch (IOException e) {
            throw new CloseWriterException(e);
        }
    }

}
