package com.example.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverManagerP {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initializeDriver() {
        String browser = System.getProperty("browser", "chrome");
        WebDriver webDriver;

        if (browser.equalsIgnoreCase("firefox")) {
            webDriver = new FirefoxDriver();
        } else {
            webDriver = new ChromeDriver();
        }

        webDriver.manage().window().maximize();
        driver.set(webDriver);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            webDriver.quit();
            driver.remove();
        }
    }
}