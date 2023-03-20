package com.ua.glebkorobov.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ua.glebkorobov.GetProperty;
import com.ua.glebkorobov.dto.Pojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.*;
import java.util.List;

public class Producer {

    private static final Logger logger = LogManager.getLogger(Producer.class);

    private GetProperty property = new GetProperty("myProp.properties");

    public void sendMessage(List<Pojo> list) throws JMSException, JsonProcessingException {
        ConnectToJMS connectToJMS = new ConnectToJMS();
        Connection producerConnection = connectToJMS.connect();

        logger.info("Created connection to jms");

        final Session producerSession = producerConnection
                .createSession(false, Session.AUTO_ACKNOWLEDGE);

        logger.info("Created session");

        final Destination producerDestination = producerSession
                .createQueue(property.getValueFromProperty("queue_name"));

        logger.info("Created destination");

        final MessageProducer producer = producerSession.createProducer(producerDestination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        logger.info("Created message consumer");

        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
//        mapper.findAndRegisterModules();

        for (Object o : list) {
            TextMessage producerMessage = producerSession.createTextMessage(mapper.writeValueAsString(o));

            producer.send(producerMessage);
            logger.info("Message sent.");
        }

        producer.close();
        producerSession.close();
        producerConnection.close();
        connectToJMS.stop();
    }
}
