// src/test/java/com/example/hooks/Hooks.java
package com.example.hooks;

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
    }

    @After("@ui")
    public void teardownUi(Scenario scenario) {
        if (scenario.isFailed()) {
            System.out.println("Test FAILED: " + scenario.getName());
            // Screenshot logic would go here
        }
        System.out.println("=== Finished UI Test: " + scenario.getName() + " ===\n");
    }

    @Before("@api")
    public void setupApi(Scenario scenario) {
        System.out.println("=== Starting API Test: " + scenario.getName() + " ===");
    }

    @After("@api")
    public void teardownApi(Scenario scenario) {
        System.out.println("=== Finished API Test: " + scenario.getName() + " ===\n");
    }
}