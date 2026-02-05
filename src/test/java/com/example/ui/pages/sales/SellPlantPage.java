package com.example.ui.pages.sales;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SellPlantPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Page heading
    @FindBy(xpath = "//h3[contains(text(), 'Sell Plant') or contains(text(), 'New Sale')]")
    private WebElement sellPlantHeading;

    // Alternative heading locators
    @FindBy(xpath = "//h1[contains(text(), 'Sell Plant') or contains(text(), 'New Sale')] | //h2[contains(text(), 'Sell Plant') or contains(text(), 'New Sale')]")
    private WebElement sellPlantHeadingAlternative;

    // Form elements
    @FindBy(xpath = "//form[contains(@action, '/sales') or contains(@class, 'form')]")
    private WebElement sellPlantForm;

    // Submit button
    @FindBy(xpath = "//button[contains(text(), 'Sell') or contains(text(), 'Submit') or contains(text(), 'Create')] | //input[@type='submit']")
    private WebElement submitButton;

    public SellPlantPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isSellPlantPageLoaded() {
        try {
            // Wait for URL to contain /ui/sales/new
            wait.until(ExpectedConditions.urlContains("/ui/sales/new"));
            
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("/ui/sales/new")) {
                return false;
            }
            
            // Check for sell plant form
            boolean hasFormElements = false;
            
            // Try to find sell plant heading
            try {
                WebElement heading = null;
                if (sellPlantHeading.isDisplayed()) {
                    heading = sellPlantHeading;
                } else if (sellPlantHeadingAlternative.isDisplayed()) {
                    heading = sellPlantHeadingAlternative;
                }
                
                if (heading != null) {
                    String headingText = heading.getText().toLowerCase();
                    if (headingText.contains("sell") || headingText.contains("new sale")) {
                        hasFormElements = true;
                    }
                }
            } catch (Exception e) {
                // Continue checking other elements
            }
            
            // Check for form element
            try {
                if (sellPlantForm.isDisplayed()) {
                    hasFormElements = true;
                }
            } catch (Exception e) {
                // Continue checking other elements
            }
            
            // Check for submit button
            try {
                if (submitButton.isDisplayed()) {
                    hasFormElements = true;
                }
            } catch (Exception e) {
                // Continue checking other elements
            }
            
            // Fallback: check page title
            if (!hasFormElements) {
                try {
                    String pageTitle = driver.getTitle().toLowerCase();
                    if (pageTitle.contains("sell") || pageTitle.contains("sale")) {
                        hasFormElements = true;
                    }
                } catch (Exception e) {
                    // Use final fallback
                }
            }
            
            return hasFormElements;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSellPlantFormLoaded() {
        try {
            // Check for form element
            if (sellPlantForm.isDisplayed()) {
                return true;
            }
            
            // Fallback: check for any form on the page
            WebElement anyForm = driver.findElement(By.xpath("//form"));
            return anyForm.isDisplayed();
            
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            return null;
        }
    }

    public void waitForPageLoad() {
        try {
            // Wait for URL to contain /ui/sales/new
            wait.until(ExpectedConditions.urlContains("/ui/sales/new"));
            
            // Try to wait for form
            try {
                wait.until(ExpectedConditions.visibilityOf(sellPlantForm));
            } catch (Exception e) {
                // Form might not be present, continue
            }
            
            // Try to wait for heading
            try {
                wait.until(ExpectedConditions.visibilityOf(sellPlantHeading));
            } catch (Exception e) {
                // Heading might not be present, continue
            }
            
            // At least wait for some content to load
            Thread.sleep(1000);
            
        } catch (Exception e) {
            // Page might still be loading, continue anyway
        }
    }
}
