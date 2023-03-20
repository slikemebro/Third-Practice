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
import java.util.ArrayList;
import java.util.List;

public class Consumer {

    private static final Logger logger = LogManager.getLogger(Consumer.class);

    private GetProperty property = new GetProperty("myProp.properties");

    public List<Pojo> receiveMessage() throws JMSException, JsonProcessingException {
        ConnectToJMS connectToJMS = new ConnectToJMS();
        Connection consumerConnection = connectToJMS.connect();

        logger.info("Created connection to jms");

        final Session consumerSession = consumerConnection
                .createSession(false, Session.AUTO_ACKNOWLEDGE);

        logger.info("Created session");

        final Destination consumerDestination = consumerSession
                .createQueue(property.getValueFromProperty("queue_name"));

        logger.info("Created destination");

        final MessageConsumer consumer = consumerSession
                .createConsumer(consumerDestination);

        logger.info("Created message consumer");

        Message consumerMessage;

        List<Pojo> receivedMessages = new ArrayList<>();

        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
//        mapper.findAndRegisterModules();

        while ((consumerMessage = consumer.receive(1000)) != null) {
            TextMessage consumerTextMessage = (TextMessage) consumerMessage;
            logger.info("Message received: {}", consumerTextMessage.getText());
            receivedMessages.add(mapper.readValue(consumerTextMessage.getText(), Pojo.class));
        }

        consumer.close();
        consumerSession.close();
        consumerConnection.close();
        connectToJMS.stop();

        return receivedMessages;
    }
}
