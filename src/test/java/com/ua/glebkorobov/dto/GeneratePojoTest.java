package com.ua.glebkorobov.dto;

import com.ua.glebkorobov.GetProperty;
import com.ua.glebkorobov.jms.Producer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.jms.JMSException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GeneratePojoTest {

//    @Mock
//    GetProperty getProperty;

    @Mock
    Producer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateMessagesTest() throws JMSException {
//        when(getProperty.getValueFromProperty("count")).thenReturn("123");

        when(producer.getCounter()).thenReturn(123L);
        GeneratePojo generatePojo = new GeneratePojo();

        assertEquals(1000, generatePojo.generateAndSendMessages());
    }
}