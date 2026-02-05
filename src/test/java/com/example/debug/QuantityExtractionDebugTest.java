package com.example.debug;

import com.example.utils.DriverManager;
import com.example.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class QuantityExtractionDebugTest {

    @Test
    public void debugQuantityExtraction() {
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
            
            System.out.println("=== QUANTITY EXTRACTION DEBUG ===");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            
            // Extract quantities using the same logic as our page object
            java.util.List<WebElement> rows = driver.findElements(By.xpath("//table//tbody//tr"));
            System.out.println("Found " + rows.size() + " table rows");
            
            for (int i = 0; i < rows.size(); i++) {
                WebElement row = rows.get(i);
                try {
                    WebElement quantityCell = row.findElement(By.xpath("./td[position()=2]"));
                    String quantityText = quantityCell.getText().trim();
                    System.out.println("Row " + (i + 1) + " quantity: '" + quantityText + "'");
                } catch (Exception e) {
                    System.out.println("Row " + (i + 1) + ": no quantity found");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Debug test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}