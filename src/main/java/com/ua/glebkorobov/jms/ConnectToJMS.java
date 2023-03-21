package com.ua.glebkorobov.jms;

import com.ua.glebkorobov.GetProperty;
import com.ua.glebkorobov.exceptions.ConnectException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

public class ConnectToJMS {

    private static GetProperty property = new GetProperty("myProp.properties");

    private final static String WIRE_LEVEL_ENDPOINT
            = property.getValueFromProperty("endpoint");
    private final static String ACTIVE_MQ_USERNAME = property.getValueFromProperty("username");
    private final static String ACTIVE_MQ_PASSWORD = property.getValueFromProperty("password");

    private PooledConnectionFactory pooledConnectionFactory;

    public Connection connect() {
        final ActiveMQConnectionFactory connectionFactory =
                createActiveMQConnectionFactory();
        pooledConnectionFactory = createPooledConnectionFactory(connectionFactory);

        final Connection connection;
        try {
            connection = pooledConnectionFactory
                    .createConnection();
            connection.start();
        } catch (JMSException e) {
            throw new ConnectException(e);
        }

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
