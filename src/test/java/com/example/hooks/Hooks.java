// src/test/java/com/example/hooks/Hooks.java
package com.example.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.example.utils.DriverManager;
import java.time.Duration;

public class Hooks {

    @Before("@ui")
    public void setupUi(Scenario scenario) {
        System.out.println("=== Starting UI Test: " + scenario.getName() + " ===");
        // Initialize WebDriver for UI tests
        DriverManager.initializeDriver();
        
        // Clean up any existing test data before running the test
        if (scenario.getName().contains("plant") || scenario.getName().contains("Plant")) {
            cleanupTestPlants(DriverManager.getDriver());
        }
    }

    @After("@ui")
    public void teardownUi(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        
        // Clean up test plants before closing driver
        if (driver != null) {
            cleanupTestPlants(driver);
            
            if (scenario.isFailed()) {
                System.out.println("Test FAILED: " + scenario.getName());
                if (driver instanceof TakesScreenshot) {
                    try {
                        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                        scenario.attach(screenshot, "image/png", scenario.getName());
                    } catch (Exception e) {
                        System.out.println("Could not capture screenshot: " + e.getMessage());
                    }
                }
            }
        }
        
        System.out.println("=== Finished UI Test: " + scenario.getName() + " ===\n");
        // Quit WebDriver
        DriverManager.quitDriver();
    }

    private void cleanupTestPlants(WebDriver driver) {
        try {
            // Navigate to plants list page
            String baseUrl = System.getProperty("base.url", "http://localhost:8080");
            driver.navigate().to(baseUrl + "/ui/plants");
            
            // Wait for page to load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            Thread.sleep(2000); // Extra wait for page to fully load
            
            // Try to find and delete test plants
            String[] testPlantNames = {"Red Rose", "Test Plant", "Sample Plant"};
            
            for (String plantName : testPlantNames) {
                try {
                    // Strategy 1: Look for row with plant name and delete button
                    String deleteXpath = "//td[contains(text(), '" + plantName + "')]//following::button[contains(@class, 'btn-danger') or contains(text(), 'Delete')]";
                    var deleteElements = driver.findElements(By.xpath(deleteXpath));
                    
                    if (deleteElements.isEmpty()) {
                        // Strategy 2: Look for action menu or dropdown
                        String actionXpath = "//td[contains(text(), '" + plantName + "')]//following::button[contains(@class, 'dropdown') or contains(@class, 'menu')]";
                        deleteElements = driver.findElements(By.xpath(actionXpath));
                    }
                    
                    if (deleteElements.isEmpty()) {
                        // Strategy 3: Look for any button in the same row
                        String rowXpath = "//tr[contains(., '" + plantName + "')]//button";
                        deleteElements = driver.findElements(By.xpath(rowXpath));
                    }
                    
                    for (var deleteBtn : deleteElements) {
                        try {
                            // Scroll to element if needed
                            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                                "arguments[0].scrollIntoView(true);", deleteBtn);
                            Thread.sleep(500);
                            deleteBtn.click();
                            
                            // Handle any confirmation dialogs
                            try {
                                Thread.sleep(500);
                                var confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
                                    By.xpath("//button[contains(text(), 'Confirm') or contains(text(), 'Yes') or contains(text(), 'OK') or contains(text(), 'Delete')]")));
                                confirmBtn.click();
                                System.out.println("Cleaned up test plant: " + plantName);
                                Thread.sleep(1000); // Wait for deletion to complete
                                break; // Plant deleted, move to next plant name
                            } catch (TimeoutException e) {
                                System.out.println("No confirmation dialog found for: " + plantName);
                            }
                        } catch (Exception e) {
                            System.out.println("Could not delete plant: " + plantName + " - " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    // Plant not found or error occurred, continue with next plant
                }
            }
        } catch (Exception e) {
            System.out.println("Could not cleanup test plants: " + e.getMessage());
        }
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