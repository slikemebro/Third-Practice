package com.ua.glebkorobov.dto;

import com.ua.glebkorobov.jms.Producer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.Session;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class GeneratePojo {

    private static final Logger logger = LogManager.getLogger(GeneratePojo.class);


    public AtomicInteger generateAndSendMessages(Producer producer, String count) throws JMSException {

        logger.info("generate stream of pojo");

        AtomicInteger atomicInteger = new AtomicInteger();


        Stream.generate(() -> new Pojo(RandomStringUtils.randomAlphabetic(4, 15), new Random().nextLong(),
                        LocalDateTime.now()))
                .limit(Long.parseLong(count))
                .forEach((p) -> {
                    producer.sendMessage(p);
                    atomicInteger.getAndIncrement();
                });

        producer.stop();

        return atomicInteger;
    }

}
