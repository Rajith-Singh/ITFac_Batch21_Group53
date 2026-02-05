package com.example.debug;

import com.example.utils.DriverManager;
import com.example.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

public class SalesTableDebugTest {

    @Test
    public void debugSalesTable() {
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
            
            System.out.println("=== SALES TABLE DEBUG ===");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page Title: " + driver.getTitle());
            
            // Check for Plant Name header
            try {
                WebElement plantHeader = driver.findElement(By.xpath("//a[contains(@href, 'sortField=plant.name') and contains(text(), 'Plant')]"));
                System.out.println("✓ Plant Name header found: " + plantHeader.getText());
                System.out.println("  Href: " + plantHeader.getAttribute("href"));
            } catch (Exception e) {
                System.out.println("✗ Plant Name header not found");
            }
            
            // Find all table rows
            try {
                WebElement table = driver.findElement(By.xpath("//table"));
                System.out.println("✓ Table found");
                
                List<WebElement> rows = driver.findElements(By.xpath("//table//tbody//tr"));
                System.out.println("Found " + rows.size() + " table rows");
                
                if (rows.isEmpty()) {
                    // Try alternative row selector
                    rows = driver.findElements(By.xpath("//table//tr"));
                    System.out.println("Found " + rows.size() + " table rows using alternative selector");
                }
                
                for (int i = 0; i < rows.size(); i++) {
                    WebElement row = rows.get(i);
                    String rowText = row.getText();
                    System.out.println("Row " + (i + 1) + ": " + rowText);
                    
                    // Try to find plant name in this row
                    try {
                        List<WebElement> cells = row.findElements(By.xpath("./td"));
                        System.out.println("  Cells: " + cells.size());
                        for (int j = 0; j < cells.size(); j++) {
                            String cellText = cells.get(j).getText().trim();
                            if (!cellText.isEmpty()) {
                                System.out.println("    Cell " + (j + 1) + ": '" + cellText + "'");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("  Could not extract cells");
                    }
                }
                
            } catch (Exception e) {
                System.out.println("✗ No table found");
            }
            
            // Look for any elements containing plant names
            String[] expectedPlants = {"Aloe Vera", "Monstera", "Snake Plant", "ZZ Plant"};
            for (String plant : expectedPlants) {
                try {
                    WebElement element = driver.findElement(By.xpath("//*[contains(text(), '" + plant + "')]"));
                    System.out.println("✓ Found element containing: " + plant);
                } catch (Exception e) {
                    System.out.println("✗ No element found containing: " + plant);
                }
            }
            
            // Print all links that might contain plant names
            try {
                List<WebElement> allLinks = driver.findElements(By.tagName("a"));
                System.out.println("\n=== ALL LINKS ===");
                for (WebElement link : allLinks) {
                    String text = link.getText().trim();
                    if (!text.isEmpty() && (text.toLowerCase().contains("aloe") || text.toLowerCase().contains("monster") || 
                        text.toLowerCase().contains("snake") || text.toLowerCase().contains("zz"))) {
                        System.out.println("  Link: '" + text + "' -> " + link.getAttribute("href"));
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not extract links");
            }
            
        } catch (Exception e) {
            System.err.println("Debug test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}