package com.ua.glebkorobov.jms;

import com.ua.glebkorobov.exceptions.CloseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerTest {

    @Test
    void closeConnectionExceptionTest() {
        Consumer consumer = new Consumer();
        assertThrows(CloseException.class, consumer::closeConnection);
    }
}