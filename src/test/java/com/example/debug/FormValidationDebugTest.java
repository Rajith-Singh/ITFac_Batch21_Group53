package com.example.debug;

import com.example.ui.pages.sales.SellPlantPage;
import com.example.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Debug test to check HTML5 form validation detection
 * Run this when the application is available
 */
public class FormValidationDebugTest {
    
    public static void main(String[] args) {
        WebDriver driver = null;
        try {
            // Initialize driver
            driver = DriverManager.getDriver();
            SellPlantPage sellPlantPage = new SellPlantPage(driver);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // Navigate directly to sell plant form (assuming already logged in)
            driver.get("http://localhost:8081/ui/sales/new");
            Thread.sleep(3000);
            
            System.out.println("=== Form Validation Debug Test ===");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            
            // Check if page loaded
            if (sellPlantPage.isSellPlantFormLoaded()) {
                System.out.println("✅ Sell Plant form loaded successfully");
                
                // Try to select a plant and enter invalid quantity
                sellPlantPage.selectPlantFromDropdown("Mango Tree");
                Thread.sleep(1000);
                sellPlantPage.enterQuantity("0");
                Thread.sleep(1000);
                
                // Try to submit form
                sellPlantPage.clickSellButton();
                Thread.sleep(3000);
                
                // Debug: Check various validation methods
                System.out.println("\n=== Validation Detection Results ===");
                
                // Method 1: HTML5 validation message
                try {
                    String validationMessage = (String) js.executeScript(
                        "return arguments[0].validationMessage;", 
                        driver.findElement(org.openqa.selenium.By.xpath("//input[contains(@name, 'quantity') or contains(@id, 'quantity')]"))
                    );
                    System.out.println("HTML5 Validation Message: " + validationMessage);
                } catch (Exception e) {
                    System.out.println("HTML5 Validation Message: Not accessible - " + e.getMessage());
                }
                
                // Method 2: Check validity state
                try {
                    Boolean isValid = (Boolean) js.executeScript(
                        "return arguments[0].checkValidity();", 
                        driver.findElement(org.openqa.selenium.By.xpath("//input[contains(@name, 'quantity') or contains(@id, 'quantity')]"))
                    );
                    System.out.println("Field Validity: " + isValid);
                } catch (Exception e) {
                    System.out.println("Field Validity: Not accessible - " + e.getMessage());
                }
                
                // Method 3: Our enhanced error detection
                boolean errorDetected = sellPlantPage.isErrorMessageDisplayed();
                String errorText = sellPlantPage.getErrorMessageText();
                System.out.println("Our Error Detection: " + errorDetected);
                System.out.println("Error Text: " + errorText);
                
                // Method 4: Check if form submission was prevented
                boolean stillOnForm = sellPlantPage.isStillOnSellPlantPage();
                System.out.println("Still on Form: " + stillOnForm);
                
                // Method 5: Get current form values
                String quantityValue = sellPlantPage.getQuantityFieldValue();
                String selectedPlant = sellPlantPage.getSelectedPlantFromDropdown();
                System.out.println("Current Quantity: " + quantityValue);
                System.out.println("Selected Plant: " + selectedPlant);
                
            } else {
                System.out.println("❌ Sell Plant form did not load");
            }
            
        } catch (Exception e) {
            System.err.println("Debug test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                // Don't close driver if running through test framework
                // driver.quit();
            }
        }
    }
}