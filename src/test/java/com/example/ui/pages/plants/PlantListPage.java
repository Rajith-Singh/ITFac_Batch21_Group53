package com.example.ui.pages.plants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class PlantListPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Page heading
    @FindBy(xpath = "//h1[contains(text(), 'Plants') or contains(text(), 'Plants List')] | //h2[contains(text(), 'Plants') or contains(text(), 'Plants List')] | //h3[contains(text(), 'Plants') or contains(text(), 'Plants List')]")
    private WebElement plantsListHeading;

    // Plants table
    @FindBy(xpath = "//table")
    private WebElement plantsTable;

    // Error messages
    @FindBy(xpath = "//div[contains(@class,'error') or contains(@class,'alert') or contains(@class,'message')] | //p[contains(@class,'error') or contains(@class,'alert') or contains(@class,'message')] | //span[contains(@class,'error') or contains(@class,'alert') or contains(@class,'message')]")
    private List<WebElement> errorMessages;

    // Access denied messages
    @FindBy(xpath = "//*[contains(text(),'access denied') or contains(text(),'Access Denied') or contains(text(),'permission') or contains(text(),'Permission') or contains(text(),'forbidden') or contains(text(),'Forbidden') or contains(text(),'unauthorized') or contains(text(),'Unauthorized')]")
    private List<WebElement> accessDeniedMessages;

    // 403 error page elements
    @FindBy(xpath = "//*[contains(text(),'403') or contains(text(),'Forbidden')]")
    private List<WebElement> forbiddenElements;

    public PlantListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isPlantsListPageLoaded() {
        try {
            // Wait for URL to contain plants
            wait.until(ExpectedConditions.urlContains("/ui/plants"));
            
            // Check if we're on plants page by URL
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("/ui/plants")) {
                return false;
            }
            
            // Try to find any plants-related element to confirm page is loaded
            boolean hasPlantsElements = false;
            
            // Check for plants heading (try multiple variations)
            try {
                WebElement heading = driver.findElement(By.xpath("//h1[contains(text(), 'Plants') or contains(text(), 'plants')] | //h2[contains(text(), 'Plants') or contains(text(), 'plants')] | //h3[contains(text(), 'Plants') or contains(text(), 'plants')]"));
                if (heading.isDisplayed()) {
                    hasPlantsElements = true;
                }
            } catch (Exception e) {
                // Continue checking other elements
            }
            
            // Check for plants table
            try {
                WebElement table = driver.findElement(By.xpath("//table"));
                if (table.isDisplayed()) {
                    hasPlantsElements = true;
                }
            } catch (Exception e) {
                // Continue checking other elements
            }
            
            // If no specific elements found, at least check if page title contains Plants
            if (!hasPlantsElements) {
                try {
                    String pageTitle = driver.getTitle();
                    if (pageTitle.toLowerCase().contains("plants")) {
                        hasPlantsElements = true;
                    }
                } catch (Exception e) {
                    // Use final fallback
                }
            }
            
            return hasPlantsElements;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForPageLoad() {
        try {
            // Wait for URL to contain plants
            wait.until(ExpectedConditions.urlContains("/ui/plants"));
            
            // Try to wait for plants table
            try {
                wait.until(ExpectedConditions.visibilityOf(plantsTable));
            } catch (Exception e) {
                // Table might not be present, continue
            }
            
            // Try to wait for heading
            try {
                wait.until(ExpectedConditions.visibilityOf(plantsListHeading));
            } catch (Exception e) {
                // Heading might not be present, continue
            }
            
            // At least wait for some content to load
            Thread.sleep(1000);
            
        } catch (Exception e) {
            // Page might still be loading, continue anyway
        }
    }

    // TC_UI_PLANTS_USER_10 - Admin restriction methods
    public boolean hasPermissionErrorMessage() {
        try {
            // Check for permission-related error messages
            List<WebElement> permissionMessages = driver.findElements(By.xpath(
                "//*[contains(text(),'permission') or contains(text(),'Permission') or " +
                "contains(text(),'access') or contains(text(),'Access') or " +
                "contains(text(),'authorized') or contains(text(),'Authorized') or " +
                "contains(text(),'admin') or contains(text(),'Admin')]"
            ));
            
            for (WebElement message : permissionMessages) {
                if (message.isDisplayed()) {
                    return true;
                }
            }
            
            // Check for error/alert elements that might contain permission messages
            if (errorMessages != null) {
                for (WebElement error : errorMessages) {
                    if (error.isDisplayed()) {
                        String errorText = error.getText().toLowerCase();
                        if (errorText.contains("permission") || errorText.contains("access") || 
                            errorText.contains("authorized") || errorText.contains("admin")) {
                            return true;
                        }
                    }
                }
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasAccessDeniedMessage() {
        try {
            if (accessDeniedMessages != null) {
                for (WebElement message : accessDeniedMessages) {
                    if (message.isDisplayed()) {
                        return true;
                    }
                }
            }
            
            // Check for 403 forbidden elements
            if (forbiddenElements != null) {
                for (WebElement element : forbiddenElements) {
                    if (element.isDisplayed()) {
                        return true;
                    }
                }
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasErrorMessage() {
        try {
            if (errorMessages != null && !errorMessages.isEmpty()) {
                for (WebElement error : errorMessages) {
                    if (error.isDisplayed()) {
                        return true;
                    }
                }
            }
            
            // Check for any error-like elements
            List<WebElement> allErrors = driver.findElements(By.xpath(
                "//div[contains(@class,'error')] | //div[contains(@class,'alert')] | " +
                "//p[contains(@class,'error')] | //p[contains(@class,'alert')] | " +
                "//span[contains(@class,'error')] | //span[contains(@class,'alert')]"
            ));
            
            for (WebElement error : allErrors) {
                if (error.isDisplayed()) {
                    return true;
                }
            }
            
            // Check for error indication in page title or URL
            try {
                String pageTitle = driver.getTitle();
                String currentUrl = driver.getCurrentUrl();
                
                if (pageTitle.toLowerCase().contains("error") || 
                    pageTitle.toLowerCase().contains("forbidden") || 
                    pageTitle.toLowerCase().contains("access denied") ||
                    currentUrl.contains("403") || 
                    currentUrl.contains("error") || 
                    currentUrl.contains("forbidden")) {
                    return true;
                }
            } catch (Exception e) {
                // Continue
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            // First try to get from specific error message elements
            if (errorMessages != null && !errorMessages.isEmpty()) {
                for (WebElement error : errorMessages) {
                    if (error.isDisplayed()) {
                        String errorText = error.getText().trim();
                        if (!errorText.isEmpty()) {
                            return errorText;
                        }
                    }
                }
            }
            
            // Try to get from access denied messages
            if (accessDeniedMessages != null && !accessDeniedMessages.isEmpty()) {
                for (WebElement message : accessDeniedMessages) {
                    if (message.isDisplayed()) {
                        String messageText = message.getText().trim();
                        if (!messageText.isEmpty()) {
                            return messageText;
                        }
                    }
                }
            }
            
            // Try to find any error-like element
            List<WebElement> allErrors = driver.findElements(By.xpath(
                "//div[contains(@class,'error') or contains(@class,'alert')] | " +
                "//p[contains(@class,'error') or contains(@class,'alert')] | " +
                "//span[contains(@class,'error') or contains(@class,'alert')]"
            ));
            
            for (WebElement error : allErrors) {
                if (error.isDisplayed()) {
                    String errorText = error.getText().trim();
                    if (!errorText.isEmpty()) {
                        return errorText;
                    }
                }
            }
            
            // Fallback: try to get page title or body content for error indication
            try {
                String pageTitle = driver.getTitle();
                if (pageTitle.toLowerCase().contains("error") || 
                    pageTitle.toLowerCase().contains("forbidden") || 
                    pageTitle.toLowerCase().contains("access denied")) {
                    return pageTitle;
                }
            } catch (Exception e) {
                // Continue
            }
            
            // Additional fallback: check for main content that might contain error message
            try {
                List<WebElement> mainContent = driver.findElements(By.xpath("//main | //body | //div[@id='app'] | //div[@class='container']"));
                for (WebElement content : mainContent) {
                    if (content.isDisplayed()) {
                        String contentText = content.getText().trim();
                        if (contentText.toLowerCase().contains("access") && 
                            contentText.toLowerCase().contains("denied")) {
                            return "Access Denied - Page Content";
                        }
                    }
                }
            } catch (Exception e) {
                // Continue
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // TC_UI_PLANTS_USER_11 - Edit Plant page restriction methods
    public boolean hasEditForm() {
        try {
            // Check for edit form elements
            List<WebElement> editForms = driver.findElements(By.xpath(
                "//form[contains(@action,'edit') or contains(@action,'update')] | " +
                "//form[contains(@id,'edit') or contains(@id,'update')] | " +
                "//div[contains(@class,'edit-form') or contains(@class,'edit-form')]"
            ));
            
            for (WebElement form : editForms) {
                if (form.isDisplayed()) {
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasEditFormFields() {
        try {
            // Check for typical edit form fields (name, description, price, etc.)
            List<WebElement> editFields = driver.findElements(By.xpath(
                "//input[@type='text' or @type='number' or @type='email'] | " +
                "//textarea | " +
                "//select | " +
                "//input[contains(@name,'name') or contains(@name,'description') or contains(@name,'price')]"
            ));
            
            for (WebElement field : editFields) {
                if (field.isDisplayed()) {
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasSaveUpdateButtons() {
        try {
            // Check for save/update buttons
            List<WebElement> saveButtons = driver.findElements(By.xpath(
                "//button[contains(text(),'Save') or contains(text(),'Update') or contains(text(),'Submit')] | " +
                "//input[@type='submit' and contains(@value,'Save') or contains(@value,'Update') or contains(@value,'Submit')] | " +
                "//a[contains(text(),'Save') or contains(text(),'Update')]"
            ));
            
            for (WebElement button : saveButtons) {
                if (button.isDisplayed() && button.isEnabled()) {
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
