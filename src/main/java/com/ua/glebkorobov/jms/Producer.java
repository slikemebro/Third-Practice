package com.ua.glebkorobov.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.jms.*;
import java.util.List;

public class Producer {


    public void sendMessage(List list) throws JMSException, JsonProcessingException {
        ConnectToJMS connectToJMS = new ConnectToJMS();
        Connection producerConnection = connectToJMS.connect();

        final Session producerSession = producerConnection
                .createSession(false, Session.AUTO_ACKNOWLEDGE);

        final Destination producerDestination = producerSession
                .createQueue("MyQueue");

        final MessageProducer producer = producerSession.createProducer(producerDestination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        for (Object o : list) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            TextMessage producerMessage = producerSession.createTextMessage(mapper.writeValueAsString(o));

            producer.send(producerMessage);
            System.out.println("Message sent.");
        }

        producer.close();
        producerSession.close();
        producerConnection.close();
        connectToJMS.stop();
    }
}
