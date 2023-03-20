package com.ua.glebkorobov.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneratePojo {

    private static final Logger logger = LogManager.getLogger(GeneratePojo.class);

    private static final String COUNT = "count";

    private GeneratePojo() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Pojo> generate(){
        logger.info("List of was generate and returned");
        return Stream
                .generate(Pojo::new)
                .limit(Long.parseLong(System.getProperty(COUNT)))
                .collect(Collectors.toList());
    }
}
