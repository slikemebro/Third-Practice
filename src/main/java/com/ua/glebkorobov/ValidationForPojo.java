package com.ua.glebkorobov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.opencsv.CSVWriter;
import com.ua.glebkorobov.dto.Pojo;
import com.ua.glebkorobov.exceptions.CsvWriteException;
import com.ua.glebkorobov.jms.Consumer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.JMSException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class ValidationForPojo {

    static String invalidWriterNameFile = "invalid.csv";
    static String validWriterNameFile = "valid.csv";

    private static final GetProperty property = new GetProperty("myProp.properties");

    private static final String POISON_PILL = property.getValueFromProperty("poison");

    private static final Logger logger = LogManager.getLogger(ValidationForPojo.class);

    public void valid() throws JMSException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        logger.info("Created validator");

        Consumer consumer = new Consumer();
        consumer.createConnection();
        logger.info("Created consumer and connection");

        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        try (CSVWriter invalidWriter = new CSVWriter(new FileWriter(invalidWriterNameFile));
             CSVWriter validWriter = new CSVWriter(new FileWriter(validWriterNameFile))) {

            invalidWriter.writeNext(new String[]{"Name", "Count", "Errors"});
            validWriter.writeNext(new String[]{"Name", "Count"});

            while (true) {
                String message = consumer.receiveMessage();
                if (message.equals(POISON_PILL)) {
                    logger.info("Got poison pill : {}", POISON_PILL);
                    break;
                }

                Pojo pojo = mapper.readValue(message, Pojo.class);
                Set<ConstraintViolation<Pojo>> violations = validator.validate(pojo);

                if (violations.isEmpty()) {
                    validWriter.writeNext(new String[]{pojo.getName(), String.valueOf(pojo.getCount())});
                } else {
                    String error = Arrays.toString(errors(violations));
                    invalidWriter.writeNext(new String[]{pojo.getName(), String.valueOf(pojo.getCount()), "errors " + error});
                }
            }
        } catch (IOException e) {
            throw new CsvWriteException(e);
        }

        consumer.closeConnection();
    }

    private static String[] errors(Set<ConstraintViolation<Pojo>> violations) {
        String[] arr = new String[violations.size()];
        int i = 0;
        for (ConstraintViolation<Pojo> violation : violations) {
            arr[i] = violation.getMessage();
            i++;
        }
        return arr;
    }

}
