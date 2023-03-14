package com.ua.glebkorobov.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

public class ConnectToJMS {

    private final static String WIRE_LEVEL_ENDPOINT
            = "ssl://b-b4af96b1-01ff-4af7-811e-1d81e6fd3f52-1.mq.us-west-2.amazonaws.com:61617";
    private final static String ACTIVE_MQ_USERNAME = "slikemebro";
    private final static String ACTIVE_MQ_PASSWORD = "Gfnhjy18011001";

    private PooledConnectionFactory pooledConnectionFactory;

    public Connection connect() throws JMSException {
        final ActiveMQConnectionFactory connectionFactory =
                createActiveMQConnectionFactory();
        pooledConnectionFactory = createPooledConnectionFactory(connectionFactory);

        final Connection connection = pooledConnectionFactory
                .createConnection();
        connection.start();

        return connection;
    }

    private PooledConnectionFactory createPooledConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
        final PooledConnectionFactory pooledConnectionFactory =
                new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(10);
        return pooledConnectionFactory;
    }

    private ActiveMQConnectionFactory createActiveMQConnectionFactory() {
        final ActiveMQConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory();

        connectionFactory.setBrokerURL(WIRE_LEVEL_ENDPOINT);
        connectionFactory.setUserName(ACTIVE_MQ_USERNAME);
        connectionFactory.setPassword(ACTIVE_MQ_PASSWORD);
        return connectionFactory;
    }

    public void stop(){
        pooledConnectionFactory.stop();
    }
}
