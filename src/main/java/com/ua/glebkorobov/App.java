package com.ua.glebkorobov;


import com.ua.glebkorobov.dto.GeneratePojo;
import com.ua.glebkorobov.dto.Pojo;
import com.ua.glebkorobov.jms.Consumer;
import com.ua.glebkorobov.jms.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main( String[] args ) throws Exception {
        List<Pojo> list = GeneratePojo.generate();

        Producer producer = new Producer();
        producer.sendMessage(list);

        Consumer consumer = new Consumer();
        List<Pojo> receivedList = consumer.receiveMessage();

        ValidationForList.valid(receivedList);
    }
}
