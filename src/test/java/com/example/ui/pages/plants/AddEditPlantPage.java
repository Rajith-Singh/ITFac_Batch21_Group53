package com.example.ui.pages.plants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import java.time.Duration;

public class AddEditPlantPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "name")
    private WebElement plantNameField;

    @FindBy(id = "categoryId")
    private WebElement categoryDropdown;

    @FindBy(id = "price")
    private WebElement priceField;

    @FindBy(id = "quantity")
    private WebElement quantityField;

    @FindBy(xpath = "//button[contains(text(), 'save') or contains(text(), 'Save')]")
    private WebElement saveButton;

    @FindBy(xpath = "//div[contains(@class, 'alert alert-success')] | //div[contains(@class, 'success')] | //div[@class='alert success'] | //span[contains(@class, 'success')]")
    private WebElement successMessage;

    public AddEditPlantPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void navigateToAddPlantPage(String baseUrl) {
        driver.navigate().to(baseUrl + "/ui/plants/add");
    }

    public boolean isAddPlantPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(plantNameField));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void enterPlantName(String plantName) {
        WebElement nameField = wait.until(ExpectedConditions.visibilityOf(plantNameField));
        nameField.clear();
        nameField.sendKeys(plantName);
    }

    public void selectCategory(String categoryName) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOf(categoryDropdown));
        Select select = new Select(dropdown);
        select.selectByVisibleText(categoryName);
    }

    public java.util.List<String> getAvailableCategories() {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOf(categoryDropdown));
        Select select = new Select(dropdown);
        java.util.List<String> options = new java.util.ArrayList<>();
        for (WebElement option : select.getOptions()) {
            String text = option.getText();
            if (text != null && !text.trim().isEmpty() && !text.contains("-- Select")) {
                options.add(text.trim());
            }
        }
        return options;
    }

    public void enterPrice(String price) {
        WebElement priceFld = wait.until(ExpectedConditions.visibilityOf(priceField));
        priceFld.clear();
        priceFld.sendKeys(price);
    }

    public void enterQuantity(String quantity) {
        WebElement quantityFld = wait.until(ExpectedConditions.visibilityOf(quantityField));
        quantityFld.clear();
        quantityFld.sendKeys(quantity);
    }

    public void clickSaveButton() {
        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        saveBtn.click();
    }

    public boolean isSuccessMessageDisplayed(String expectedMessage) {
        try {
            WebElement message = wait.until(ExpectedConditions.visibilityOf(successMessage));
            return message.getText().contains(expectedMessage) || message.getText().contains("successfully");
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(successMessage)).getText();
        } catch (Exception e) {
            // If success message not found, check for error messages
            return getErrorMessage();
        }
    }

    public String getAlertText() {
        try {
            org.openqa.selenium.Alert alert = driver.switchTo().alert();
            String text = alert.getText();
            return text;
        } catch (Exception e) {
            return null;
        }
    }

    public String getErrorMessage() {
        try {
            // Try different error message selectors
            WebElement errorMsg = driver.findElement(By.xpath(
                    "//div[contains(@class, 'alert alert-danger')] | //div[contains(@class, 'error')] | //span[@class='error']"));
            return "ERROR: " + errorMsg.getText();
        } catch (Exception e) {
            // Check for validation error messages on form fields
            try {
                WebElement formError = driver.findElement(By.xpath(
                        "//*[contains(text(), 'error') or contains(text(), 'Error') or contains(text(), 'required')]"));
                return "VALIDATION ERROR: " + formError.getText();
            } catch (Exception ex) {
                return "No messages found on page";
            }
        }
    }

    // Validation helpers
    public String getValidationMessageForField(String fieldId) {
        try {
            // Check for HTML5 validation message property (browser native)
            WebElement element = driver.findElement(By.id(fieldId));
            String validationMessage = element.getAttribute("validationMessage");
            if (validationMessage != null && !validationMessage.isEmpty()) {
                return validationMessage;
            }

            // common patterns for validation messages: small, div.invalid-feedback,
            // span.error
            String xpath = "(//*[@id='" + fieldId + "']/following::small|//*[@id='" + fieldId
                    + "']/following::div[contains(@class,'invalid-feedback')]|//*[@id='" + fieldId
                    + "']/following::span[contains(@class,'error')])[1]";
            WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            return msg.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean hasValidationMessage(String fieldId, String expectedMessage) {
        String actual = getValidationMessageForField(fieldId);
        if (actual == null)
            actual = "";
        return actual.trim().length() > 0 && (actual.contains(expectedMessage) || expectedMessage.contains(actual)
                || actual.toLowerCase().contains(expectedMessage.toLowerCase()));
    }

    // Broad page-level message check (fallback)
    public boolean hasMessageOnPage(String expectedMessage) {
        try {
            return wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), expectedMessage));
        } catch (Exception e) {
            return false;
        }
    }

    // Check if the input/select field is marked as required (HTML5 or ARIA)
    public boolean fieldIsMarkedRequired(String fieldId) {
        try {
            WebElement el = driver.findElement(By.id(fieldId));
            String req = el.getAttribute("required");
            String aria = el.getAttribute("aria-required");
            if (req != null && (req.equalsIgnoreCase("true") || req.length() > 0))
                return true;
            if (aria != null && aria.equalsIgnoreCase("true"))
                return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
