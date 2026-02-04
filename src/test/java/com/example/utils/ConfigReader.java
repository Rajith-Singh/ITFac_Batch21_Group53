package com.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static final String TESTDATA_FILE_PATH = "src/main/resources/testdata.properties";

    static {
        properties = new Properties();
        loadProperties();
    }

    private static void loadProperties() {
        try {
            FileInputStream configFile = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(configFile);
            configFile.close();

            FileInputStream testDataFile = new FileInputStream(TESTDATA_FILE_PATH);
            properties.load(testDataFile);
            testDataFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration files");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
