package com.ua.glebkorobov;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.ua.glebkorobov.dto.GeneratePojo;
import com.ua.glebkorobov.dto.Pojo;
import com.ua.glebkorobov.jms.Consumer;
import com.ua.glebkorobov.jms.Producer;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws JMSException, JsonProcessingException {
        GeneratePojo generatePojo = new GeneratePojo();

        List<Pojo> list = generatePojo.generate(100);

        Producer producer = new Producer();
        producer.sendMessage(list);

        Consumer consumer = new Consumer();
        List<Pojo> receivedList = consumer.receiveMessage();

        ValidationForList.valid(receivedList);
    }
}
