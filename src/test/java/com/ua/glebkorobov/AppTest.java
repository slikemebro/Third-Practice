package com.ua.glebkorobov;


import com.ua.glebkorobov.dto.GeneratePojo;
import com.ua.glebkorobov.jms.ConnectToJMS;
import org.junit.jupiter.api.Test;

import javax.jms.JMSException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    void shouldAnswerWithTrue() {
        assertTrue( true );
    }

    @Test
    void testGeneratePojo() {
        System.setProperty("count", "100");
//        assertEquals(100, GeneratePojo.generate().size());
    }

    @Test
    void testConnectToJMS() throws JMSException {
        ConnectToJMS connectToJMS = new ConnectToJMS();
        connectToJMS.connect();
    }
}
