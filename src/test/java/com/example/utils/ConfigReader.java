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
        resolveVariables();
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

    private static void resolveVariables() {
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if (value != null && value.contains("${")) {
                // Replace ${variable} with actual values
                for (String varKey : properties.stringPropertyNames()) {
                    String varPattern = "${" + varKey + "}";
                    if (value.contains(varPattern)) {
                        String varValue = properties.getProperty(varKey);
                        if (varValue != null) {
                            value = value.replace(varPattern, varValue);
                        }
                    }
                }
                properties.setProperty(key, value);
            }
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in configuration files");
        }
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}