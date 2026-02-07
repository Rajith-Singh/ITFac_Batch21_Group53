// src/test/java/com/example/hooks/Hooks.java
package com.example.hooks;

import com.example.api.utils.TokenContext;
import com.example.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hooks {

    @Before("@ui")
    public void setupUi(Scenario scenario) {
        System.out.println("=== Starting UI Test: " + scenario.getName() + " ===");
        // WebDriver is initialized lazily by DriverManager when first accessed
    }

    @After("@ui")
    public void teardownUi(Scenario scenario) {
        if (scenario.isFailed()) {
            System.out.println("Test FAILED: " + scenario.getName());
            // Take screenshot on failure
            try {
                WebDriver driver = DriverManager.getDriver();
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot on Failure");
            } catch (Exception e) {
                System.out.println("Failed to take screenshot: " + e.getMessage());
            }
        }
        System.out.println("=== Finished UI Test: " + scenario.getName() + " ===\n");
        // Quit WebDriver after each scenario
        DriverManager.quitDriver();
    }

    @Before("@api")
    public void setupApi(Scenario scenario) {
        System.out.println("=== Starting API Test: " + scenario.getName() + " ===");
    }

    @After("@api")
    public void teardownApi(Scenario scenario) {
        TokenContext.clear();
        System.out.println("=== Finished API Test: " + scenario.getName() + " ===\n");
    }
}