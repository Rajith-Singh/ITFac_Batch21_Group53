package com.example.api.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApiConfig {
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/api-config.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load api-config.properties");
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("api.base.url");
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
