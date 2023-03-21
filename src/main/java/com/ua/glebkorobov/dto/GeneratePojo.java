package com.ua.glebkorobov.dto;

import com.ua.glebkorobov.GetProperty;
import com.ua.glebkorobov.jms.Producer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.JMSException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Stream;

public class GeneratePojo {

    private static final Logger logger = LogManager.getLogger(GeneratePojo.class);

    private final GetProperty property = new GetProperty("myProp.properties");


    public long generateAndSendMessages() throws JMSException {
        Producer producer = new Producer();
        producer.createConnection();

        logger.info("generate stream of pojo");

        Stream.generate(() -> new Pojo(RandomStringUtils.randomAlphabetic(4, 15), new Random().nextLong(),
                        LocalDateTime.now()))
                .limit(Long.parseLong(property.getValueFromProperty("count")))
                .forEach(producer::sendMessage);

        producer.stop();

        return producer.getCounter();
    }
}
