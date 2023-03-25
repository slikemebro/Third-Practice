package com.ua.glebkorobov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.opencsv.CSVWriter;
import com.ua.glebkorobov.dto.Pojo;
import com.ua.glebkorobov.exceptions.CsvWriteException;
import com.ua.glebkorobov.exceptions.ParsePojoException;
import com.ua.glebkorobov.jms.Consumer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.JMSException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Set;

public class ValidationForPojo {

    String invalidPojoWriterFile = "invalid.csv";
    String validPojoWriterFile = "valid.csv";

    private final GetProperty property = new GetProperty("myProp.properties");

    private final String poisonPill = property.getValueFromProperty("poison");

    private final Logger logger = LogManager.getLogger(ValidationForPojo.class);

    private final Validator validator = createValidator();


    public void receiveAndValid() throws JMSException {
        Consumer consumer = new Consumer();
        consumer.createConnection();
        logger.info("Created consumer and connection");

        ObjectMapper mapper = createMapper();

        CSVWriter validWriter = createValidCsvWriter();
        CSVWriter invalidWriter = createInValidCsvWriter();

        invalidWriter.writeNext(new String[]{"Name", "Count", "Errors"});
        validWriter.writeNext(new String[]{"Name", "Count"});

        Pojo pojo;
        Set<ConstraintViolation<Pojo>> violations;

        while (true) {
            String message = consumer.receiveMessage();
            if (message.equals(poisonPill)) {
                logger.info("Got poison pill : {}", poisonPill);
                consumer.closeConnection();
                break;
            }

            try {
                pojo = mapper.readValue(message, Pojo.class);
            } catch (Exception e) {
                throw new ParsePojoException(e);
            }
            violations = valid(pojo);

            if (violations.isEmpty()) {
                writeValidFile(pojo, validWriter);
            } else {
                writeInvalidFile(pojo, violations, invalidWriter);
            }
        }
    }

    public Set<ConstraintViolation<Pojo>> valid(Pojo pojo) {
        return validator.validate(pojo);
    }


    public ObjectMapper createMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    public Validator createValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    public CSVWriter createInValidCsvWriter() {
        try {
            return new CSVWriter(new FileWriter(invalidPojoWriterFile));
        } catch (Exception e) {
            throw new CsvWriteException(e);
        }
    }

    public CSVWriter createValidCsvWriter() {
        try {
            return new CSVWriter(new FileWriter(validPojoWriterFile));
        } catch (Exception e) {
            throw new CsvWriteException(e);
        }
    }

    public void writeInvalidFile(Pojo pojo, Set<ConstraintViolation<Pojo>> violations, CSVWriter invalidWriter) {
        String error = Arrays.toString(createErrors(violations));
        invalidWriter.writeNext(new String[]{pojo.getName(), String.valueOf(pojo.getCount()),
                "errors " + error});
    }

    public void writeValidFile(Pojo pojo, CSVWriter validWriter) {
        validWriter.writeNext(new String[]{pojo.getName(), String.valueOf(pojo.getCount())});
    }


    public String[] createErrors(Set<ConstraintViolation<Pojo>> violations) {
        String[] arr = new String[violations.size()];
        int i = 0;
        for (ConstraintViolation<Pojo> violation : violations) {
            arr[i] = violation.getMessage();
            i++;
        }
        return arr;
    }

}
