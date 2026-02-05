package com.example.debug;

import com.example.utils.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class SalesPageDebugTest {

    @Test
    public void debugSalesPage() {
        WebDriver driver = DriverManager.getDriver();
        
        try {
            // Navigate to sales page
            driver.get("http://localhost:8081/ui/sales");
            Thread.sleep(3000);
            
            System.out.println("Current URL: " + driver.getCurrentUrl());
            System.out.println("Page Title: " + driver.getTitle());
            
            // Check all elements that might be present
            try {
                WebElement heading = driver.findElement(By.xpath("//h1 | //h2 | //h3 | //h4 | //h5 | //h6"));
                System.out.println("Found heading: " + heading.getText());
            } catch (Exception e) {
                System.out.println("No heading found");
            }
            
            try {
                WebElement table = driver.findElement(By.xpath("//table"));
                System.out.println("Table found: " + table.isDisplayed());
            } catch (Exception e) {
                System.out.println("No table found");
            }
            
            try {
                WebElement plantLink = driver.findElement(By.xpath("//a[contains(@href, 'sortField=plant.name')]"));
                System.out.println("Plant sort link found: " + plantLink.getText() + " | " + plantLink.getAttribute("href"));
            } catch (Exception e) {
                System.out.println("No plant sort link found");
            }
            
            // Print all links on the page
            java.util.List<WebElement> links = driver.findElements(By.tagName("a"));
            System.out.println("Total links found: " + links.size());
            for (WebElement link : links) {
                String href = link.getAttribute("href");
                String text = link.getText().trim();
                if (href != null && (href.contains("sales") || href.contains("plant") || text.contains("Sales") || text.contains("Plant"))) {
                    System.out.println("  Link: '" + text + "' -> " + href);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Debug test failed: " + e.getMessage());
        }
    }
}