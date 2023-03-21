package com.ua.glebkorobov.jms;


import com.ua.glebkorobov.GetProperty;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.*;

public class Consumer {

    private static final Logger logger = LogManager.getLogger(Consumer.class);

    private final GetProperty property = new GetProperty("myProp.properties");

    private MessageConsumer messageConsumer;

    private Session consumerSession;

    private Connection consumerConnection;


    private final String myQueue = property.getValueFromProperty("queue_name");

    public MessageConsumer createConnection() throws JMSException {
        ActiveMQConnectionFactory connectionFactory =
                createActiveMQConnectionFactory();

        consumerConnection = connectionFactory.createConnection();
        consumerConnection.start();

        // Create a session.
        consumerSession = consumerConnection
                .createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create a queue named "MyQueue".
        final Destination consumerDestination = consumerSession
                .createQueue(myQueue);

        // Create a message consumer from the session to the queue.
        messageConsumer = consumerSession
                .createConsumer(consumerDestination);

        logger.info("Created consumer connection");

        return messageConsumer;
    }

    public String receiveMessage() throws JMSException {
        // Begin to wait for messages.
        Message consumerMessage = messageConsumer.receive(1000);

        // Receive the message when it arrives.
        TextMessage consumerTextMessage = (TextMessage) consumerMessage;
        return consumerTextMessage.getText();
    }

    public Connection closeConnection() throws JMSException {
        messageConsumer.close();
        consumerSession.close();
        consumerConnection.close();
        logger.info("Closed consumer connection");
        return consumerConnection;
    }


    private ActiveMQConnectionFactory createActiveMQConnectionFactory() {
        // Create a connection factory.
        final ActiveMQConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(property.getValueFromProperty("endpoint"));

        // Pass the sign-in credentials.
        connectionFactory.setUserName(property.getValueFromProperty("username"));
        connectionFactory.setPassword(property.getValueFromProperty("password"));
        return connectionFactory;
    }

}
