package com.ua.glebkorobov.jms;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ua.glebkorobov.dto.Pojo;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class Consumer {
    public List<Pojo> receiveMessage() throws JMSException, JsonProcessingException {
        ConnectToJMS connectToJMS = new ConnectToJMS();
        Connection consumerConnection = connectToJMS.connect();

        final Session consumerSession = consumerConnection
                .createSession(false, Session.AUTO_ACKNOWLEDGE);

        final Destination consumerDestination = consumerSession
                .createQueue("MyQueue");

        final MessageConsumer consumer = consumerSession
                .createConsumer(consumerDestination);

        Message consumerMessage;

        List<Pojo> receivedMessages = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        while ((TextMessage)(consumerMessage = consumer.receive()) != null) {
            TextMessage consumerTextMessage = (TextMessage) consumerMessage;
            System.out.println("Message received: " + consumerTextMessage.getText());
            receivedMessages.add(mapper.readValue(((TextMessage) consumerMessage).getText(), Pojo.class));
        }

        consumer.close();
        consumerSession.close();
        consumerConnection.close();
        connectToJMS.stop();

        return receivedMessages;
    }
}
