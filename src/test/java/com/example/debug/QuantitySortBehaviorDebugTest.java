package com.example.debug;

import com.example.utils.DriverManager;
import com.example.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class QuantitySortBehaviorDebugTest {

    @Test
    public void debugQuantitySortBehavior() {
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
            
            System.out.println("=== QUANTITY SORT BEHAVIOR DEBUG ===");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            
            // Check initial Quantity header URL
            try {
                WebElement quantityHeader = driver.findElement(By.xpath("//a[contains(@href, 'sortField=quantity') and contains(text(), 'Quantity')]"));
                String initialHref = quantityHeader.getAttribute("href");
                System.out.println("Initial Quantity header href: " + initialHref);
            } catch (Exception e) {
                System.out.println("Could not find Quantity header: " + e.getMessage());
            }
            
            // Click once for first sort
            try {
                WebElement quantityHeader = driver.findElement(By.xpath("//a[contains(@href, 'sortField=quantity') and contains(text(), 'Quantity')]"));
                quantityHeader.click();
                Thread.sleep(2000);
                
                String firstClickHref = quantityHeader.getAttribute("href");
                System.out.println("After first click href: " + firstClickHref);
            } catch (Exception e) {
                System.out.println("Could not click Quantity header: " + e.getMessage());
            }
            
            // Click again for second sort
            try {
                WebElement quantityHeader = driver.findElement(By.xpath("//a[contains(@href, 'sortField=quantity') and contains(text(), 'Quantity')]"));
                quantityHeader.click();
                Thread.sleep(2000);
                
                String secondClickHref = quantityHeader.getAttribute("href");
                System.out.println("After second click href: " + secondClickHref);
            } catch (Exception e) {
                System.out.println("Could not click Quantity header again: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("Debug test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}