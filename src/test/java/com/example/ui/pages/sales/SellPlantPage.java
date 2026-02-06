package com.example.ui.pages.sales;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import com.example.utils.EnhancedWaitUtils;

import java.time.Duration;
import java.util.Set;

public class SellPlantPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    private EnhancedWaitUtils enhancedWait;

    // Page heading
    @FindBy(xpath = "//h3[contains(text(), 'Sell Plant') or contains(text(), 'New Sale')]")
    private WebElement sellPlantHeading;

    // Alternative heading locators
    @FindBy(xpath = "//h1[contains(text(), 'Sell Plant') or contains(text(), 'New Sale')] | //h2[contains(text(), 'Sell Plant') or contains(text(), 'New Sale')]")
    private WebElement sellPlantHeadingAlternative;

    // Form elements
    @FindBy(xpath = "//form[contains(@action, '/sales') or contains(@class, 'form')]")
    private WebElement sellPlantForm;

    // Form fields
    @FindBy(xpath = "//select[contains(@name, 'plant') or contains(@id, 'plant')] | //label[contains(text(), 'Plant')]/following-sibling::select | //label[contains(text(), 'Plant')]/../select")
    private WebElement plantDropdown;
    
    @FindBy(xpath = "//input[contains(@name, 'quantity') or contains(@id, 'quantity') or contains(@placeholder, 'quantity')] | //label[contains(text(), 'Quantity')]/following-sibling::input | //label[contains(text(), 'Quantity')]/../input")
    private WebElement quantityField;
    
    // Submit button
    @FindBy(xpath = "//button[contains(text(), 'Sell') or contains(text(), 'Submit') or contains(text(), 'Create')] | //input[@type='submit']")
    private WebElement submitButton;
    
    // Error messages - expanded locators
    @FindBy(xpath = "//div[contains(@class, 'error') or contains(@class, 'alert-danger') or contains(@class, 'invalid-feedback') or contains(@class, 'text-danger')] | //span[contains(@class, 'error') or contains(@class, 'text-danger')] | //small[contains(@class, 'error') or contains(@class, 'text-danger')]")
    private WebElement errorMessage;
    
    // Plant specific error message - expanded locators
    @FindBy(xpath = "//select[contains(@name, 'plant') or contains(@id, 'plant')]/following-sibling::*[contains(@class, 'error') or contains(@class, 'invalid-feedback') or contains(@class, 'text-danger')] | //select[contains(@name, 'plant') or contains(@id, 'plant')]/../*[contains(@class, 'error') or contains(@class, 'invalid-feedback') or contains(@class, 'text-danger')] | //label[contains(text(), 'Plant')]/following-sibling::*[contains(@class, 'error') or contains(@class, 'invalid-feedback') or contains(@class, 'text-danger')]")
    private WebElement plantErrorMessage;

    public SellPlantPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Increased from 10 to 30 seconds
        this.js = (JavascriptExecutor) driver;
        this.enhancedWait = new EnhancedWaitUtils(driver);
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
            
            // Wait for page to be ready
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            // Try to wait for form with increased timeout
            try {
                wait.until(ExpectedConditions.visibilityOf(sellPlantForm));
                System.out.println("Sell plant form is visible");
            } catch (Exception e) {
                System.out.println("Sell plant form not immediately visible, waiting for alternative elements...");
                // Wait for any form to appear as fallback
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("form")));
            }
            
            // Try to wait for heading
            try {
                wait.until(ExpectedConditions.visibilityOf(sellPlantHeading));
                System.out.println("Sell plant heading is visible");
            } catch (Exception e) {
                // Try alternative heading
                try {
                    wait.until(ExpectedConditions.visibilityOf(sellPlantHeadingAlternative));
                    System.out.println("Alternative sell plant heading is visible");
                } catch (Exception ex) {
                    System.out.println("No heading found, continuing with form elements...");
                }
            }
            
            // Wait for key form elements to be interactive
            try {
                wait.until(ExpectedConditions.elementToBeClickable(plantDropdown));
                System.out.println("Plant dropdown is clickable");
            } catch (Exception e) {
                System.out.println("Plant dropdown not immediately clickable, continuing...");
            }
            
            try {
                wait.until(ExpectedConditions.elementToBeClickable(quantityField));
                System.out.println("Quantity field is clickable");
            } catch (Exception e) {
                System.out.println("Quantity field not immediately clickable, continuing...");
            }
            
            try {
                wait.until(ExpectedConditions.elementToBeClickable(submitButton));
                System.out.println("Submit button is clickable");
            } catch (Exception e) {
                System.out.println("Submit button not immediately clickable, continuing...");
            }
            
            // Additional wait for slow JavaScript
            Thread.sleep(3000);
            
        } catch (Exception e) {
            System.out.println("Page load wait interrupted: " + e.getMessage());
            // Additional wait as fallback
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    // Form interaction methods
    public void leavePlantDropdownEmpty() {
        try {
            // Find the plant dropdown and ensure no selection is made
            if (plantDropdown.isDisplayed()) {
                // Get current selection to verify it's empty
                String selectedValue = plantDropdown.getAttribute("value");
                System.out.println("Plant dropdown current value: " + selectedValue);
            }
        } catch (Exception e) {
            System.out.println("Plant dropdown not found or not visible: " + e.getMessage());
        }
    }
    
    public void enterQuantity(String quantity) {
        try {
            if (quantityField.isDisplayed()) {
                quantityField.clear();
                quantityField.sendKeys(quantity);
                System.out.println("Entered quantity: " + quantity);
            }
        } catch (Exception e) {
            System.out.println("Quantity field not found or not visible: " + e.getMessage());
        }
    }
    
    public void clickSellButton() {
        try {
            if (submitButton.isDisplayed() && submitButton.isEnabled()) {
                submitButton.click();
                System.out.println("Clicked Sell button");
            }
        } catch (Exception e) {
            System.out.println("Sell button not found or not clickable: " + e.getMessage());
        }
    }
    
    // Validation methods
    public boolean isFormSubmitted() {
        try {
            // Check if URL changed away from /ui/sales/new
            String currentUrl = driver.getCurrentUrl();
            return !currentUrl.contains("/ui/sales/new");
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isStillOnSellPlantPage() {
        try {
            String currentUrl = driver.getCurrentUrl();
            return currentUrl.contains("/ui/sales/new");
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isPlantErrorMessageDisplayed() {
        try {
            // Wait a moment for error messages to appear
            Thread.sleep(2000);
            
            // Try specific plant error message first
            if (plantErrorMessage.isDisplayed()) {
                System.out.println("Plant error message found and displayed");
                return true;
            }
            
            // Fallback to general error message
            if (errorMessage.isDisplayed()) {
                System.out.println("General error message found and displayed");
                return true;
            }
            
            // Additional fallback: look for any error element near plant dropdown
            try {
                WebElement plantError = driver.findElement(By.xpath("//select[contains(@name, 'plant') or contains(@id, 'plant')]/following-sibling::*[contains(@class, 'error') or contains(@class, 'invalid') or contains(@class, 'text-danger')]"));
                if (plantError.isDisplayed()) {
                    System.out.println("Plant error message found via fallback");
                    return true;
                }
            } catch (Exception e) {
                // Continue to other checks
            }
            
            // Check for any element with error text
            try {
                WebElement errorWithText = driver.findElement(By.xpath("//*[contains(text(), 'required') or contains(text(), 'Required') or contains(text(), 'Plant')][contains(@class, 'error') or contains(@class, 'invalid') or contains(@class, 'text-danger')]"));
                if (errorWithText.isDisplayed()) {
                    System.out.println("Error message found via text search");
                    return true;
                }
            } catch (Exception e) {
                // Continue to other checks
            }
            
            System.out.println("No error message found");
            return false;
            
        } catch (Exception e) {
            System.out.println("Error checking for error message: " + e.getMessage());
            return false;
        }
    }
    
    public String getPlantErrorMessageText() {
        try {
            // Wait a moment for error messages to appear
            Thread.sleep(1000);
            
            // Try specific plant error message first
            if (plantErrorMessage.isDisplayed()) {
                String text = plantErrorMessage.getText().trim();
                if (!text.isEmpty()) {
                    System.out.println("Plant error message found: " + text);
                    return text;
                }
            }
            
            // Fallback to general error message
            if (errorMessage.isDisplayed()) {
                String text = errorMessage.getText().trim();
                if (!text.isEmpty()) {
                    System.out.println("General error message found: " + text);
                    return text;
                }
            }
            
            // Look for any element containing "Plant is required"
            try {
                WebElement errorWithText = driver.findElement(By.xpath("//*[contains(text(), 'Plant is required') or contains(text(), 'plant is required')]"));
                if (errorWithText.isDisplayed()) {
                    String text = errorWithText.getText().trim();
                    System.out.println("Error message found via text search: " + text);
                    return text;
                }
            } catch (Exception e) {
                // Continue to other checks
            }
            
            // Additional fallback: look for any error element near plant dropdown
            try {
                WebElement plantError = driver.findElement(By.xpath("//select[contains(@name, 'plant') or contains(@id, 'plant')]/following-sibling::*[contains(@class, 'error') or contains(@class, 'invalid') or contains(@class, 'text-danger')]"));
                if (plantError.isDisplayed()) {
                    String text = plantError.getText().trim();
                    if (!text.isEmpty()) {
                        System.out.println("Plant error message found via fallback: " + text);
                        return text;
                    }
                }
            } catch (Exception e) {
                // Continue to other checks
            }
            
            System.out.println("No error message text found");
            return null;
            
        } catch (Exception e) {
            System.out.println("Error getting error message text: " + e.getMessage());
            return null;
        }
    }
    
    public boolean isSellButtonEnabled() {
        try {
            return submitButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    // TC_UI_SAL_16 specific methods
    public boolean isPlantAvailableInDropdown(String plantName) {
        try {
            if (plantDropdown.isDisplayed()) {
                java.util.List<WebElement> options = plantDropdown.findElements(By.tagName("option"));
                for (WebElement option : options) {
                    String optionText = option.getText().trim();
                    if (optionText.contains(plantName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking plant availability: " + e.getMessage());
        }
        return false;
    }

    public String getPlantStockFromDropdown(String plantName) {
        try {
            if (plantDropdown.isDisplayed()) {
                java.util.List<WebElement> options = plantDropdown.findElements(By.tagName("option"));
                System.out.println("DEBUG: Found " + options.size() + " dropdown options:");
                
                for (WebElement option : options) {
                    String optionText = option.getText().trim();
                    System.out.println("DEBUG: Option text: '" + optionText + "'");
                    
                    if (optionText.contains(plantName)) {
                        System.out.println("DEBUG: Found plant " + plantName + " in dropdown");
                        
                        // Extract stock from format like "Mango Tree (10 in stock)"
                        if (optionText.contains("(") && optionText.contains("in stock)")) {
                            String stockPart = optionText.substring(optionText.indexOf("(") + 1, optionText.indexOf(" in stock)"));
                            System.out.println("DEBUG: Extracted stock: " + stockPart.trim());
                            return stockPart.trim();
                        } else if (optionText.contains("(Stock:")) {
                            // Try format like "Mango Tree (Stock: 10)"
                            String stockPart = optionText.substring(optionText.indexOf("(Stock:") + 8, optionText.indexOf(")"));
                            System.out.println("DEBUG: Extracted stock (Stock: format): " + stockPart.trim());
                            return stockPart.trim();
                        } else if (optionText.contains("(") && optionText.contains(")")) {
                            // Try alternative format like "Mango Tree (10)"
                            String stockPart = optionText.substring(optionText.indexOf("(") + 1, optionText.indexOf(")"));
                            System.out.println("DEBUG: Extracted stock (alt format): " + stockPart.trim());
                            return stockPart.trim();
                        } else {
                            System.out.println("DEBUG: No stock information found in option text");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting plant stock: " + e.getMessage());
        }
        return null;
    }

    public void selectPlantFromDropdown(String plantName) {
        try {
            if (plantDropdown.isDisplayed()) {
                java.util.List<WebElement> options = plantDropdown.findElements(By.tagName("option"));
                for (WebElement option : options) {
                    String optionText = option.getText().trim();
                    if (optionText.contains(plantName)) {
                        option.click();
                        System.out.println("Selected plant: " + plantName);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error selecting plant from dropdown: " + e.getMessage());
        }
    }

    public boolean isSaleSuccessful() {
        try {
            // Wait for potential redirect or success message
            Thread.sleep(2000);
            
            // Check if redirected away from sell form (indicating success)
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("/ui/sales/new")) {
                return true;
            }
            
            // Check for success message
            try {
                WebElement successMessage = driver.findElement(By.xpath("//*[contains(@class, 'success') or contains(@class, 'alert-success') or contains(text(), 'success') or contains(text(), 'Success')]"));
                if (successMessage.isDisplayed()) {
                    return true;
                }
            } catch (Exception e) {
                // No success message found
            }
            
            // Check if form is reset (another indicator of success)
            try {
                String selectedValue = plantDropdown.getAttribute("value");
                if (selectedValue == null || selectedValue.isEmpty()) {
                    return true;
                }
            } catch (Exception e) {
                // Cannot check form reset
            }
            
        } catch (Exception e) {
            System.out.println("Error checking sale success: " + e.getMessage());
        }
        return false;
    }

    public void navigateToSellPlantForm() {
        try {
            // If we're already on the page, just refresh
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("/ui/sales/new")) {
                refreshPage();
            } else {
                // Navigate to the sell plant form
                driver.get("http://localhost:8081/ui/sales/new");
                waitForPageLoad();
            }
            System.out.println("Navigated to Sell Plant form");
        } catch (Exception e) {
            System.out.println("Error navigating to sell plant form: " + e.getMessage());
        }
    }

    // Enhanced stock verification method
    public int getPlantStockValue(String plantName) {
        String stockText = getPlantStockFromDropdown(plantName);
        if (stockText != null) {
            try {
                return Integer.parseInt(stockText.trim());
            } catch (NumberFormatException e) {
                System.out.println("Could not parse stock value for " + plantName + ": " + stockText);
                return -1;
            }
        }
        return -1;
    }

    // Enhanced verification of stock display format
    public boolean verifyPlantStockDisplay(String plantName, int expectedStock) {
        try {
            String displayText = getPlantDisplayTextInDropdown(plantName);
            if (displayText != null) {
                // Check for expected format
                boolean containsStockInfo = displayText.contains("(" + expectedStock + " in stock)") ||
                                          displayText.contains("(Stock: " + expectedStock + ")") ||
                                          displayText.contains("(" + expectedStock + ")");
                
                if (containsStockInfo) {
                    System.out.println("Stock display verified: " + displayText);
                    return true;
                } else {
                    System.out.println("Stock display format incorrect. Expected stock " + expectedStock + 
                                     " in format, but found: " + displayText);
                }
            }
        } catch (Exception e) {
            System.out.println("Error verifying stock display: " + e.getMessage());
        }
        return false;
    }

    public void openNewTabAndNavigateToSellPlantForm() {
        try {
            // Store current handle
            String currentHandle = driver.getWindowHandle();
            
            // Open new tab using JavaScript
            js.executeScript("window.open('http://localhost:8081/ui/sales/new', '_blank');");
            
            // Wait for new tab to open
            Thread.sleep(2000);
            
            // Switch to new tab
            Set<String> handles = driver.getWindowHandles();
            for (String handle : handles) {
                if (!handle.equals(currentHandle)) {
                    driver.switchTo().window(handle);
                    break;
                }
            }
            
            // Wait for page to load
            waitForPageLoad();
            
            System.out.println("Opened new tab and navigated to Sell Plant form");
            
        } catch (Exception e) {
            System.out.println("Error opening new tab: " + e.getMessage());
            // Fallback: navigate in current tab
            driver.get("http://localhost:8081/ui/sales/new");
            waitForPageLoad();
        }
    }

        public void switchToTab(String windowHandle) {
        try {
            driver.switchTo().window(windowHandle);
            System.out.println("Switched to tab with handle: " + windowHandle);
        } catch (Exception e) {
            System.out.println("Error switching to tab: " + e.getMessage());
        }
    }

    public String getCurrentWindowHandle() {
        return driver.getWindowHandle();
    }

    public Set<String> getAllWindowHandles() {
        return driver.getWindowHandles();
    }

    public void refreshPage() {
        try {
            driver.navigate().refresh();
            // Wait for page to fully reload
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            Thread.sleep(1000); // Additional wait for dynamic content
            System.out.println("Page refreshed successfully");
        } catch (Exception e) {
            System.out.println("Error refreshing page: " + e.getMessage());
        }
    }

    public boolean isPageLoadedSuccessfully() {
        try {
            // Wait for page to be ready
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
            // Check if we're on the correct page
            String currentUrl = driver.getCurrentUrl();
            boolean isCorrectPage = currentUrl.contains("/ui/sales/new");
            
            // Check if form elements are present
            boolean hasFormElements = isSellPlantFormLoaded();
            
            return isCorrectPage && hasFormElements;
            
        } catch (Exception e) {
            System.out.println("Error checking page load: " + e.getMessage());
            return false;
        }
    }

    public String getPlantDisplayTextInDropdown(String plantName) {
        try {
            if (plantDropdown.isDisplayed()) {
                java.util.List<WebElement> options = plantDropdown.findElements(By.tagName("option"));
                for (WebElement option : options) {
                    String optionText = option.getText().trim();
                    if (optionText.contains(plantName)) {
                        return optionText;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting plant display text: " + e.getMessage());
        }
        return null;
    }

    // Debugging methods
    public void debugPageElements() {
        try {
            System.out.println("=== DEBUG: Page Elements ===");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            
            // Check form elements
            try {
                WebElement form = driver.findElement(By.xpath("//form"));
                System.out.println("Form found: " + form.isDisplayed());
            } catch (Exception e) {
                System.out.println("Form not found");
            }
            
            // Check plant dropdown
            try {
                WebElement plant = driver.findElement(By.xpath("//select | //select[contains(@name, 'plant')] | //select[contains(@id, 'plant')]"));
                System.out.println("Plant dropdown found: " + plant.isDisplayed());
                System.out.println("Plant dropdown value: " + plant.getAttribute("value"));
                
                // Debug dropdown options
                java.util.List<WebElement> options = plant.findElements(By.tagName("option"));
                System.out.println("Dropdown options found: " + options.size());
                for (WebElement option : options) {
                    System.out.println("  Option: '" + option.getText() + "'");
                }
            } catch (Exception e) {
                System.out.println("Plant dropdown not found");
            }
            
            // Check quantity field
            try {
                WebElement qty = driver.findElement(By.xpath("//input | //input[contains(@name, 'quantity')] | //input[contains(@id, 'quantity')]"));
                System.out.println("Quantity field found: " + qty.isDisplayed());
                System.out.println("Quantity field value: " + qty.getAttribute("value"));
            } catch (Exception e) {
                System.out.println("Quantity field not found");
            }
            
            // Check for any error messages
            try {
                java.util.List<WebElement> errors = driver.findElements(By.xpath("//*[contains(@class, 'error') or contains(@class, 'invalid') or contains(@class, 'alert') or contains(@class, 'text-danger')]"));
                System.out.println("Error elements found: " + errors.size());
                for (WebElement error : errors) {
                    try {
                        System.out.println("Error text: '" + error.getText() + "' (displayed: " + error.isDisplayed() + ")");
                    } catch (Exception e) {
                        System.out.println("Error element found but cannot get text");
                    }
                }
            } catch (Exception e) {
                System.out.println("No error elements found");
            }
            
            // Check page source for error patterns
            String pageSource = driver.getPageSource();
            if (pageSource.toLowerCase().contains("error") || pageSource.toLowerCase().contains("required") || pageSource.toLowerCase().contains("invalid")) {
                System.out.println("Page source contains error-related text");
            }
            
            System.out.println("=== END DEBUG ===");
        } catch (Exception e) {
            System.out.println("Debug failed: " + e.getMessage());
        }
    }

    // =============================================
    // TC_UI_SAL_17 Additional Methods
    // =============================================
    
    public boolean isErrorMessageDisplayed() {
        try {
            // Wait a moment for error messages to appear
            Thread.sleep(2000);
            
            // First check for HTML5 validation messages on quantity field
            try {
                if (quantityField.isDisplayed()) {
                    // Check if quantity field has validation errors
                    String validationMessage = (String) js.executeScript("return arguments[0].validationMessage;", quantityField);
                    if (validationMessage != null && !validationMessage.trim().isEmpty()) {
                        System.out.println("HTML5 validation message found: " + validationMessage);
                        return true;
                    }
                }
            } catch (Exception e) {
                // Continue to other checks
            }
            
            // Try general error message first
            if (errorMessage.isDisplayed()) {
                System.out.println("General error message found and displayed");
                return true;
            }
            
            // Check for quantity-specific error messages
            try {
                WebElement quantityError = driver.findElement(By.xpath("//input[contains(@name, 'quantity') or contains(@id, 'quantity')]/following-sibling::*[contains(@class, 'error') or contains(@class, 'invalid-feedback') or contains(@class, 'text-danger')]"));
                if (quantityError.isDisplayed()) {
                    System.out.println("Quantity error message found");
                    return true;
                }
            } catch (Exception e) {
                // Continue to other checks
            }
            
            // Check for any element with error text containing quantity or validation messages
            try {
                WebElement validationError = driver.findElement(By.xpath("//*[contains(text(), 'Quantity must be') or contains(text(), 'quantity must be') or contains(text(), 'greater than') or contains(text(), 'Value must be') or contains(text(), 'invalid')][contains(@class, 'error') or contains(@class, 'invalid') or contains(@class, 'text-danger') or contains(@class, 'alert')]"));
                if (validationError.isDisplayed()) {
                    System.out.println("Validation error message found via text search");
                    return true;
                }
            } catch (Exception e) {
                // Continue to other checks
            }
            
            // Check for HTML5 validation tooltips or messages in the page
            try {
                WebElement html5ValidationError = driver.findElement(By.xpath("//*[contains(text(), 'Value must be greater than or equal to 1') or contains(text(), 'greater than or equal to 1') or contains(text(), 'Please enter a value') or contains(text(), 'Please enter a number')]"));
                if (html5ValidationError.isDisplayed()) {
                    System.out.println("HTML5 validation message found via text search");
                    return true;
                }
            } catch (Exception e) {
                // Continue to other checks
            }
            
            System.out.println("No error message found");
            return false;
            
        } catch (Exception e) {
            System.out.println("Error checking for error message: " + e.getMessage());
            return false;
        }
    }

    public String getErrorMessageText() {
        try {
            // Wait a moment for error messages to appear
            Thread.sleep(1000);
            
            // First check for HTML5 validation messages on quantity field
            try {
                if (quantityField.isDisplayed()) {
                    // Check if quantity field has validation errors
                    String validationMessage = (String) js.executeScript("return arguments[0].validationMessage;", quantityField);
                    if (validationMessage != null && !validationMessage.trim().isEmpty()) {
                        System.out.println("HTML5 validation message found: " + validationMessage);
                        return validationMessage.trim();
                    }
                }
            } catch (Exception e) {
                // Continue to other checks
            }
            
            // Try general error message first
            if (errorMessage.isDisplayed()) {
                String text = errorMessage.getText().trim();
                if (!text.isEmpty()) {
                    System.out.println("General error message found: " + text);
                    return text;
                }
            }
            
            // Check for quantity-specific error messages
            try {
                WebElement quantityError = driver.findElement(By.xpath("//input[contains(@name, 'quantity') or contains(@id, 'quantity')]/following-sibling::*[contains(@class, 'error') or contains(@class, 'invalid-feedback') or contains(@class, 'text-danger')]"));
                if (quantityError.isDisplayed()) {
                    String text = quantityError.getText().trim();
                    if (!text.isEmpty()) {
                        System.out.println("Quantity error message found: " + text);
                        return text;
                    }
                }
            } catch (Exception e) {
                // Continue to other checks
            }
            
            // Look for any element containing quantity validation text
            try {
                WebElement validationError = driver.findElement(By.xpath("//*[contains(text(), 'Quantity must be') or contains(text(), 'quantity must be') or contains(text(), 'greater than') or contains(text(), 'Value must be') or contains(text(), 'invalid')]"));
                if (validationError.isDisplayed()) {
                    String text = validationError.getText().trim();
                    if (!text.isEmpty()) {
                        System.out.println("Validation error message found via text search: " + text);
                        return text;
                    }
                }
            } catch (Exception e) {
                // Continue to other checks
            }
            
            // Check for HTML5 validation tooltips or messages in the page
            try {
                WebElement html5ValidationError = driver.findElement(By.xpath("//*[contains(text(), 'Value must be greater than or equal to 1') or contains(text(), 'greater than or equal to 1') or contains(text(), 'Please enter a value') or contains(text(), 'Please enter a number')]"));
                if (html5ValidationError.isDisplayed()) {
                    String text = html5ValidationError.getText().trim();
                    System.out.println("HTML5 validation message found via text search: " + text);
                    return text;
                }
            } catch (Exception e) {
                // Continue to other checks
            }
            
            System.out.println("No error message text found");
            return null;
            
        } catch (Exception e) {
            System.out.println("Error getting error message text: " + e.getMessage());
            return null;
        }
    }

    public String getSelectedPlantFromDropdown() {
        try {
            if (plantDropdown.isDisplayed()) {
                System.out.println("DEBUG: Checking selected plant from dropdown...");
                
                // Method 1: Get the selected option using @selected attribute
                try {
                    WebElement selectedOption = plantDropdown.findElement(By.xpath(".//option[@selected]"));
                    if (selectedOption != null) {
                        String text = selectedOption.getText().trim();
                        System.out.println("DEBUG: Found selected option via @selected: " + text);
                        if (!text.isEmpty()) {
                            return text;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("DEBUG: No option with @selected attribute found");
                }
                
                // Method 2: Get the value attribute from dropdown
                try {
                    String selectedValue = plantDropdown.getAttribute("value");
                    System.out.println("DEBUG: Dropdown value attribute: " + selectedValue);
                    if (selectedValue != null && !selectedValue.isEmpty()) {
                        // Find the option with this value
                        java.util.List<WebElement> options = plantDropdown.findElements(By.tagName("option"));
                        System.out.println("DEBUG: Found " + options.size() + " options in dropdown");
                        
                        for (WebElement option : options) {
                            String optionValue = option.getAttribute("value");
                            String optionText = option.getText().trim();
                            System.out.println("DEBUG: Option - Value: " + optionValue + ", Text: " + optionText);
                            
                            if (selectedValue.equals(optionValue) || (optionValue == null && selectedValue.isEmpty())) {
                                System.out.println("DEBUG: Found matching option: " + optionText);
                                return optionText;
                            }
                        }
                        
                        // Fallback: return the value if it looks like a display value
                        if (Character.isLetter(selectedValue.charAt(0))) {
                            System.out.println("DEBUG: Returning value as display text: " + selectedValue);
                            return selectedValue;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("DEBUG: Error getting dropdown value: " + e.getMessage());
                }
                
                // Method 3: Check first non-empty option (for debugging)
                try {
                    java.util.List<WebElement> options = plantDropdown.findElements(By.tagName("option"));
                    for (int i = 0; i < options.size(); i++) {
                        WebElement option = options.get(i);
                        String text = option.getText().trim();
                        if (!text.isEmpty() && !text.contains("Select")) {
                            System.out.println("DEBUG: First non-empty option: " + text);
                            // Don't return this as selected, just for debugging
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("DEBUG: Error checking options: " + e.getMessage());
                }
                
                // Method 4: Check if any option is selected via JavaScript
                try {
                    String selectedIndex = (String) js.executeScript("return arguments[0].selectedIndex;", plantDropdown);
                    System.out.println("DEBUG: Selected index from JavaScript: " + selectedIndex);
                    if (selectedIndex != null && !selectedIndex.equals("-1")) {
                        int index = Integer.parseInt(selectedIndex);
                        java.util.List<WebElement> options = plantDropdown.findElements(By.tagName("option"));
                        if (index < options.size()) {
                            WebElement selectedOption = options.get(index);
                            String text = selectedOption.getText().trim();
                            System.out.println("DEBUG: Selected option via JavaScript: " + text);
                            return text;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("DEBUG: Error getting selected index via JavaScript: " + e.getMessage());
                }
                
                System.out.println("DEBUG: No selected plant found in dropdown");
            } else {
                System.out.println("DEBUG: Plant dropdown is not displayed");
            }
        } catch (Exception e) {
            System.out.println("Error getting selected plant: " + e.getMessage());
        }
        return null;
    }

    public String getQuantityFieldValue() {
        try {
            // Find quantity field fresh each time
            WebElement qtyField = driver.findElement(By.xpath("//input[contains(@name, 'quantity') or contains(@id, 'quantity') or contains(@placeholder, 'quantity')] | //label[contains(text(), 'Quantity')]/following-sibling::input | //label[contains(text(), 'Quantity')]/../input"));
            
            if (qtyField.isDisplayed()) {
                String value = qtyField.getAttribute("value");
                System.out.println("DEBUG: Quantity field value: " + value);
                return value;
            } else {
                System.out.println("DEBUG: Quantity field is not displayed");
            }
        } catch (Exception e) {
            System.out.println("Error getting quantity field value: " + e.getMessage());
        }
        return null;
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            // Wait a moment for success messages to appear
            Thread.sleep(2000);
            
            // Check for success message
            try {
                WebElement successMessage = driver.findElement(By.xpath("//*[contains(@class, 'success') or contains(@class, 'alert-success') or contains(text(), 'success') or contains(text(), 'Success') or contains(text(), 'created') or contains(text(), 'completed')]"));
                if (successMessage.isDisplayed()) {
                    System.out.println("Success message found and displayed");
                    return true;
                }
            } catch (Exception e) {
                // No success message found
            }
            
            // Check for confirmation page elements
            try {
                WebElement confirmationElement = driver.findElement(By.xpath("//*[contains(text(), 'Thank') or contains(text(), 'Order') or contains(text(), 'Sale') or contains(text(), 'Confirmation') or contains(text(), 'Complete')]"));
                if (confirmationElement.isDisplayed()) {
                    System.out.println("Confirmation page element found");
                    return true;
                }
            } catch (Exception e) {
                // No confirmation element found
            }
            
            System.out.println("No success message or confirmation element found");
            return false;
            
        } catch (Exception e) {
            System.out.println("Error checking for success message: " + e.getMessage());
            return false;
        }
    }
}
