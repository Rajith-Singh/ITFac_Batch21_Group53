package com.example.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Utility class to verify element locators for Plant Name sorting
 */
public class ElementLocatorVerifier {
    
    private WebDriver driver;
    private WebDriverWait wait;

    public ElementLocatorVerifier(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Verify the Plant Name header element exists and is clickable
     */
    public boolean verifyPlantNameHeader() {
        try {
            // Primary locator
            WebElement primary = driver.findElement(By.xpath("//a[@class='text-white text-decoration-none' and contains(@href, '/ui/sales') and contains(text(), 'Plant')]"));
            if (primary.isDisplayed() && primary.isEnabled()) {
                System.out.println("✓ Primary locator found: " + primary.getAttribute("href"));
                return true;
            }
        } catch (Exception e) {
            System.out.println("✗ Primary locator not found");
        }

        try {
            // Alternative locator
            WebElement alternative = driver.findElement(By.xpath("//a[contains(@href, 'sortField=plant.name') and contains(text(), 'Plant')]"));
            if (alternative.isDisplayed() && alternative.isEnabled()) {
                System.out.println("✓ Alternative locator found: " + alternative.getAttribute("href"));
                return true;
            }
        } catch (Exception e) {
            System.out.println("✗ Alternative locator not found");
        }

        try {
            // Fallback locator
            WebElement fallback = driver.findElement(By.xpath("//a[contains(@href, '/ui/sales') and (contains(@href, 'sortField=plant.name') or contains(@href, 'sortDir=')) and contains(text(), 'Plant')]"));
            if (fallback.isDisplayed() && fallback.isEnabled()) {
                System.out.println("✓ Fallback locator found: " + fallback.getAttribute("href"));
                return true;
            }
        } catch (Exception e) {
            System.out.println("✗ Fallback locator not found");
        }

        return false;
    }

    /**
     * Get current sort direction from the Plant Name header
     */
    public String getCurrentSortDirection() {
        try {
            WebElement header = driver.findElement(By.xpath("//a[contains(@href, 'sortField=plant.name') and contains(text(), 'Plant')]"));
            String href = header.getAttribute("href");
            
            if (href.contains("sortDir=desc")) {
                return "desc";
            } else if (href.contains("sortDir=asc")) {
                return "asc";
            }
            return "none";
        } catch (Exception e) {
            return "none";
        }
    }

    /**
     * Check if sales table is present
     */
    public boolean verifySalesTable() {
        try {
            WebElement table = driver.findElement(By.xpath("//table"));
            return table.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Print all link elements that might be sorting headers
     */
    public void printAllSortingLinks() {
        try {
            java.util.List<WebElement> links = driver.findElements(By.xpath("//a[contains(@href, 'sortField=')]"));
            System.out.println("Found " + links.size() + " sorting links:");
            
            for (WebElement link : links) {
                try {
                    System.out.println("  - Text: '" + link.getText() + "' | Href: " + link.getAttribute("href"));
                } catch (Exception e) {
                    System.out.println("  - Link with error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("No sorting links found");
        }
    }
}