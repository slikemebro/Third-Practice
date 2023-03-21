package com.ua.glebkorobov;


import com.ua.glebkorobov.dto.GeneratePojo;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {

        GeneratePojo generatePojo = new GeneratePojo();

        generatePojo.generateAndSendMessages();

        ValidationForPojo validationForPojo = new ValidationForPojo();

        validationForPojo.valid();

    }
}
