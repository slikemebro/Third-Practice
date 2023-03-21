package com.ua.glebkorobov.jms;

import com.ua.glebkorobov.dto.Pojo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.jms.JMSException;
import javax.jms.MessageProducer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProducerTest {

    Producer producer;

    @BeforeEach
    void setUp(){
        producer = new Producer();
    }

    @Test
    void createConnectionTest() throws JMSException {
        MessageProducer messageConsumer = producer.createConnection();
        messageConsumer.setPriority(2);
        assertEquals(2, messageConsumer.getPriority());
    }

    @Test
    void sendMessageTest() throws JMSException {
        producer.createConnection();
        assertEquals(1, producer.sendMessage(new Pojo()));
    }

    @Test
    void PooledConnectionFactoryTest() throws JMSException {
        producer.createConnection();
        assertEquals(0, producer.stop().getNumConnections());
    }
}