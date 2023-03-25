package com.ua.glebkorobov.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PojoTest {

    Pojo test = new Pojo();

    @Test
    void getName() {
        test.setName("test");
        assertEquals("test", test.getName());
    }

    @Test
    void getCount() {
        test.setCount(12);
        assertEquals(12, test.getCount());
    }

    @Test
    void getDate() {
        LocalDateTime actual = LocalDateTime.now();
        LocalDateTime expected = actual;
        test.setDateTime(actual);
        assertEquals(expected, test.getDateTime());
    }
}