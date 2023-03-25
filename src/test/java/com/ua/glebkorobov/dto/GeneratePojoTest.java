package com.ua.glebkorobov.dto;

import com.ua.glebkorobov.jms.Producer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.jms.JMSException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;

class GeneratePojoTest {


    @Test
    void generateMessagesTest() throws JMSException {
        Producer producer = Mockito.mock(Producer.class);

        String count = "3";

        Mockito.when(producer.sendMessage(any())).thenReturn(1L);

        GeneratePojo generatePojo = new GeneratePojo();

        Integer actual = generatePojo.generateAndSendMessages(producer, count).intValue();

        assertEquals(Integer.parseInt(count), actual);
    }
}