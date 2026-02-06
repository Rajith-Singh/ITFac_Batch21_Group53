package com.example.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Enhanced Wait Utility for handling slow loading pages
 */
public class EnhancedWaitUtils {
    
    private WebDriver driver;
    private WebDriverWait longWait;
    private WebDriverWait mediumWait;
    private WebDriverWait shortWait;
    
    public EnhancedWaitUtils(WebDriver driver) {
        this.driver = driver;
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(60));    // 60 seconds for very slow operations
        this.mediumWait = new WebDriverWait(driver, Duration.ofSeconds(30));  // 30 seconds for normal operations
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(15));   // 15 seconds for quick operations
    }
    
    /**
     * Wait for page to be fully loaded and ready
     */
    public void waitForPageToLoad() {
        try {
            // Wait for document ready state
            longWait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            // Additional wait for slow JavaScript
            Thread.sleep(2000);
            
            System.out.println("Page is fully loaded and ready");
        } catch (Exception e) {
            System.out.println("Page load wait interrupted: " + e.getMessage());
            // Fallback wait
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                // Restore interrupted status
            }
        }
    }
    
    /**
     * Wait for element to be visible and clickable
     */
    public WebElement waitForElementToBeClickable(By locator) {
        try {
            WebElement element = mediumWait.until(ExpectedConditions.elementToBeClickable(locator));
            System.out.println("Element is clickable: " + locator);
            return element;
        } catch (Exception e) {
            System.out.println("Element not clickable within timeout: " + locator + " - " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Wait for element to be visible
     */
    public WebElement waitForElementToBeVisible(By locator) {
        try {
            WebElement element = mediumWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            System.out.println("Element is visible: " + locator);
            return element;
        } catch (Exception e) {
            System.out.println("Element not visible within timeout: " + locator + " - " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Wait for URL to contain specific path
     */
    public void waitForUrlToContain(String urlPart) {
        try {
            longWait.until(ExpectedConditions.urlContains(urlPart));
            System.out.println("URL contains expected path: " + urlPart);
        } catch (Exception e) {
            System.out.println("URL does not contain expected path within timeout: " + urlPart + " - " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Wait for URL to change from current URL
     */
    public void waitForUrlToChange(String currentUrl) {
        try {
            longWait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl)));
            System.out.println("URL has changed from: " + currentUrl);
        } catch (Exception e) {
            System.out.println("URL did not change within timeout from: " + currentUrl + " - " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Enhanced navigation with wait
     */
    public void navigateAndWait(String url) {
        try {
            driver.get(url);
            
            // Wait for navigation to complete
            longWait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            // Additional wait for slow loading
            Thread.sleep(3000);
            
            System.out.println("Navigation completed: " + url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Navigation interrupted: " + e.getMessage());
            throw new RuntimeException("Navigation interrupted", e);
        } catch (Exception e) {
            System.out.println("Navigation failed or timed out: " + url + " - " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Safe click with wait
     */
    public void safeClick(By locator) {
        try {
            WebElement element = waitForElementToBeClickable(locator);
            element.click();
            System.out.println("Successfully clicked element: " + locator);
            
            // Wait for any post-click operations
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Click operation interrupted: " + locator + " - " + e.getMessage());
            throw new RuntimeException("Click operation interrupted", e);
        } catch (Exception e) {
            System.out.println("Failed to click element: " + locator + " - " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Safe send keys with wait
     */
    public void safeSendKeys(By locator, String text) {
        try {
            WebElement element = waitForElementToBeVisible(locator);
            element.clear();
            element.sendKeys(text);
            System.out.println("Successfully entered text in element: " + locator + " -> " + text);
        } catch (Exception e) {
            System.out.println("Failed to enter text in element: " + locator + " - " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get text safely with wait
     */
    public String safeGetText(By locator) {
        try {
            WebElement element = waitForElementToBeVisible(locator);
            String text = element.getText().trim();
            System.out.println("Retrieved text from element: " + locator + " -> " + text);
            return text;
        } catch (Exception e) {
            System.out.println("Failed to get text from element: " + locator + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get attribute value safely with wait
     */
    public String safeGetAttribute(By locator, String attribute) {
        try {
            WebElement element = waitForElementToBeVisible(locator);
            String value = element.getAttribute(attribute);
            System.out.println("Retrieved attribute from element: " + locator + " [" + attribute + "] -> " + value);
            return value;
        } catch (Exception e) {
            System.out.println("Failed to get attribute from element: " + locator + " - " + e.getMessage());
            return null;
        }
    }
}