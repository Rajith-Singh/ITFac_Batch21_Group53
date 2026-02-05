package com.example.debug;

import com.example.utils.DriverManager;
import com.example.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class QuantityColumnDebugTest {

    @Test
    public void debugQuantityColumn() {
        WebDriver driver = DriverManager.getDriver();
        
        try {
            // Login first
            String loginUrl = ConfigReader.getProperty("login.url", "http://localhost:8081/ui/login");
            String username = ConfigReader.getProperty("user.username", "testuser");
            String password = ConfigReader.getProperty("user.password", "test123");
            
            driver.get(loginUrl);
            Thread.sleep(2000);
            
            WebElement usernameField = driver.findElement(By.name("username"));
            WebElement passwordField = driver.findElement(By.name("password"));
            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
            
            usernameField.sendKeys(username);
            passwordField.sendKeys(password);
            loginButton.click();
            Thread.sleep(3000);
            
            // Navigate to sales page
            driver.get("http://localhost:8081/ui/sales");
            Thread.sleep(3000);
            
            System.out.println("=== QUANTITY COLUMN DEBUG ===");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page Title: " + driver.getTitle());
            
            // Find all table headers to locate Quantity
            try {
                java.util.List<WebElement> headers = driver.findElements(By.xpath("//table//th"));
                System.out.println("Found " + headers.size() + " table headers:");
                for (int i = 0; i < headers.size(); i++) {
                    String headerText = headers.get(i).getText().trim();
                    System.out.println("  Header " + (i + 1) + ": '" + headerText + "'");
                    
                    // Check for Quantity column
                    if (headerText.equalsIgnoreCase("Quantity") || headerText.toLowerCase().contains("quantity")) {
                        WebElement headerLink = headers.get(i).findElement(By.tagName("a"));
                        if (headerLink.isDisplayed()) {
                            System.out.println("    ✓ Found Quantity header link: " + headerLink.getAttribute("href"));
                        }
                    }
                }
                
                // Find all table rows and extract quantities
                java.util.List<WebElement> rows = driver.findElements(By.xpath("//table//tbody//tr"));
                System.out.println("Found " + rows.size() + " table rows:");
                
                for (int i = 0; i < rows.size(); i++) {
                    WebElement row = rows.get(i);
                    java.util.List<WebElement> cells = row.findElements(By.tagName("td"));
                    System.out.println("Row " + (i + 1) + ":");
                    
                    for (int j = 0; j < cells.size(); j++) {
                        String cellText = cells.get(j).getText().trim();
                        System.out.println("  Cell " + (j + 1) + ": '" + cellText + "'");
                    }
                    System.out.println();
                }
                
            } catch (Exception e) {
                System.out.println("Error analyzing table: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("Debug test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}