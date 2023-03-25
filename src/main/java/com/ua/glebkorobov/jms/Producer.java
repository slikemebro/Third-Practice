package com.ua.glebkorobov.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ua.glebkorobov.GetProperty;
import com.ua.glebkorobov.dto.Pojo;
import com.ua.glebkorobov.exceptions.CreateTextMessageException;
import com.ua.glebkorobov.exceptions.SendMessageException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

public class Producer {

    private final Logger logger = LogManager.getLogger(Producer.class);

    private final GetProperty property = new GetProperty("myProp.properties");

    private Session producerSession;

    private StopWatch watch;

    private MessageProducer messageProducer;

    private Connection producerConnection;

    private PooledConnectionFactory pooledConnectionFactory;

    private long counter = 0;

    private final long timeMax = Long.parseLong(property.getValueFromProperty("time"));

    private final long count = Long.parseLong(property.getValueFromProperty("count"));

    private final String myQueue = property.getValueFromProperty("queue_name");


    public MessageProducer createConnection() throws JMSException {

        producerSession = createSessionMethod();


        // Create a queue named "MyQueue".
        Destination producerDestination = producerSession.createQueue(myQueue);

        // Create a producer from the session to the queue.
        messageProducer = producerSession.createProducer(producerDestination);

        messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        watch = StopWatch.createStarted();

        return messageProducer;
    }

    public Session createSessionMethod() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = createActiveMQConnectionFactory();

        pooledConnectionFactory = createPooledConnectionFactory(connectionFactory);

        // Establish a connection for the producer.
        producerConnection = pooledConnectionFactory.createConnection();
        producerConnection.start();
        // Create a session.
        return producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public long sendMessage(Pojo pojo) {

        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        if (watch.getTime(TimeUnit.SECONDS) < timeMax ||
                counter < count) {
            TextMessage producerMessage;
            try {
                producerMessage = producerSession.createTextMessage(mapper.writeValueAsString(pojo));
            } catch (JsonProcessingException | JMSException e) {
                throw new CreateTextMessageException(e);
            }

            try {
                messageProducer.send(producerMessage);
            } catch (JMSException e) {
                throw new SendMessageException(e);
            }
            counter++;
        } else {
            try {
                messageProducer.send(producerSession.createTextMessage(property.getValueFromProperty("poison")));
                logger.info("poison pill sent into sendMessage");
            } catch (JMSException e) {
                throw new SendMessageException(e);
            }
        }
        return counter;
    }

    public PooledConnectionFactory stop() throws JMSException {
        double rps = (double) counter / watch.getTime(TimeUnit.SECONDS);
        logger.info("counter = {}, time = {}, rps = {}", counter, watch.getTime(TimeUnit.SECONDS), rps);
        try {
            messageProducer.send(producerSession.createTextMessage(property.getValueFromProperty("poison")));
            logger.info("poison pill sent");
        } catch (JMSException e) {
            throw new SendMessageException(e);
        }
        messageProducer.close();
        producerSession.close();
        producerConnection.close();
        pooledConnectionFactory.stop();
        return pooledConnectionFactory;
    }

    public PooledConnectionFactory
    createPooledConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
        // Create a pooled connection factory.
        pooledConnectionFactory =
                new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(10);
        return pooledConnectionFactory;
    }

    public ActiveMQConnectionFactory createActiveMQConnectionFactory() {
        // Create a connection factory.
        ActiveMQConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(property.getValueFromProperty("endpoint"));

        // Pass the sign-in credentials.
        connectionFactory.setUserName(property.getValueFromProperty("username"));
        connectionFactory.setPassword(property.getValueFromProperty("password"));
        return connectionFactory;
    }

    public long getCounter() {
        return counter;
    }

    public long getCount() {
        return count;
    }
}
