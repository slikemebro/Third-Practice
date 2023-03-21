package com.ua.glebkorobov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public class GetProperty {
    private String fileNameProperty;
    private static final Logger logger = LogManager.getLogger(GetProperty.class);
    private final Properties properties = new Properties();

    public GetProperty(String fileNameProperty) {
        logger.info("Loaded properties file");
        this.fileNameProperty = fileNameProperty;
        loadFile();
    }

    private void loadFile() {
        try {
            InputStream is = GetProperty.class.getResourceAsStream("/" + fileNameProperty);
            properties.load(is);
            logger.info("Getting properties");
        } catch (Exception e) {
            logger.warn("Properties hasn't found");
            throw new RuntimeException("File hasn't found");
        }
    }

    public String getValueFromProperty(String key) {
        logger.info("Got value from properties");
        return properties.getProperty(key.toLowerCase(Locale.ROOT));
    }

    public String getFileNameProperty() {
        logger.info("Got file name");
        return fileNameProperty;
    }

    public void setFileNameProperty(String fileNameProperty) {
        logger.info("Load properties with other name: {}", fileNameProperty);
        this.fileNameProperty = fileNameProperty;
        loadFile();
    }
}
