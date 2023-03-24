package com.ua.glebkorobov;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class ValidationForPojo {

    String invalidWriterNameFile = "invalid.csv";
    String validWriterNameFile = "valid.csv";

    private static final GetProperty property = new GetProperty("myProp.properties");

    private static final String POISON_PILL = property.getValueFromProperty("poison");

    private static final Logger logger = LogManager.getLogger(ValidationForPojo.class);

//    private CSVWriter invalidWriter;
//    private CSVWriter validWriter;

    public String valid() throws JMSException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        logger.info("Created validator");

        Consumer consumer = new Consumer();
        consumer.createConnection();
        logger.info("Created consumer and connection");

        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        CSVWriter validWriter = createValidCsvWriter();
        CSVWriter invalidWriter = createInValidCsvWriter();

        invalidWriter.writeNext(new String[]{"Name", "Count", "Errors"});
        validWriter.writeNext(new String[]{"Name", "Count"});

        while (true) {
            String message = consumer.receiveMessage();
            if (message.equals(POISON_PILL)) {
                logger.info("Got poison pill : {}", POISON_PILL);
                consumer.closeConnection();
                return POISON_PILL;
            }

            Pojo pojo;
            try {
                pojo = mapper.readValue(message, Pojo.class);
            } catch (JsonProcessingException e) {
                throw new ParsePojoException(e);
            }
            Set<ConstraintViolation<Pojo>> violations = validator.validate(pojo);

            if (violations.isEmpty()) {
                writeValidFile(pojo, validWriter);
            } else {
                writeInvalidFile(pojo, violations, invalidWriter);
            }
        }

    }

    private CSVWriter createInValidCsvWriter() {
        try {
            return new CSVWriter(new FileWriter(invalidWriterNameFile));
        } catch (IOException e) {
            throw new CsvWriteException(e);
        }
    }

    public CSVWriter createValidCsvWriter() {
        try {
            return new CSVWriter(new FileWriter(validWriterNameFile));
        } catch (IOException e) {
            throw new CsvWriteException(e);
        }
    }

    public int writeInvalidFile(Pojo pojo, Set<ConstraintViolation<Pojo>> violations, CSVWriter invalidWriter) {
        String error = Arrays.toString(errors(violations));
        invalidWriter.writeNext(new String[]{pojo.getName(), String.valueOf(pojo.getCount()), "errors " + error});

        return violations.size();
    }

    public int writeValidFile(Pojo pojo, CSVWriter validWriter) {
        validWriter.writeNext(new String[]{pojo.getName(), String.valueOf(pojo.getCount())});

        return 0;
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
