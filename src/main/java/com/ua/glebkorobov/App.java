package com.ua.glebkorobov;


import com.ua.glebkorobov.dto.GeneratePojo;
import com.ua.glebkorobov.jms.Producer;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {

        GeneratePojo generatePojo = new GeneratePojo();

        Producer producer = new Producer();

        producer.createConnection();


        GetProperty property = new GetProperty("myProp.properties");

        String count = property.getValueFromProperty("count");

        generatePojo.generateAndSendMessages(producer, count);

        ValidationForPojo validationForPojo = new ValidationForPojo();

        validationForPojo.valid();

    }
}
