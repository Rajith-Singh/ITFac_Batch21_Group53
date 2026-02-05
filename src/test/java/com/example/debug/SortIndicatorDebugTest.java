package com.example.debug;

import com.example.utils.DriverManager;
import com.example.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class SortIndicatorDebugTest {

    @Test
    public void debugSortIndicator() {
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
            
            System.out.println("=== SORT INDICATOR DEBUG ===");
            System.out.println("Initial URL: " + driver.getCurrentUrl());
            
            // Find the Plant Name header
            WebElement plantHeader = driver.findElement(By.xpath("//a[contains(@href, 'sortField=plant.name') and contains(text(), 'Plant')]"));
            String href = plantHeader.getAttribute("href");
            System.out.println("Plant Header Href: " + href);
            
            // Click once for ascending
            plantHeader.click();
            Thread.sleep(2000);
            
            System.out.println("After first click URL: " + driver.getCurrentUrl());
            
            // Find the updated header
            plantHeader = driver.findElement(By.xpath("//a[contains(@href, 'sortField=plant.name') and contains(text(), 'Plant')]"));
            href = plantHeader.getAttribute("href");
            System.out.println("Plant Header Href after first click: " + href);
            
            // Click again for descending
            plantHeader.click();
            Thread.sleep(2000);
            
            System.out.println("After second click URL: " + driver.getCurrentUrl());
            
            // Find the final header
            plantHeader = driver.findElement(By.xpath("//a[contains(@href, 'sortField=plant.name') and contains(text(), 'Plant')]"));
            href = plantHeader.getAttribute("href");
            System.out.println("Plant Header Href after second click: " + href);
            
            // Check our logic
            if (href != null) {
                if (href.contains("sortDir=desc")) {
                    System.out.println("✓ Our logic would detect: DESC");
                } else if (href.contains("sortDir=asc")) {
                    System.out.println("✓ Our logic would detect: ASC");
                } else {
                    System.out.println("✗ No sortDir found in href");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Debug test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}