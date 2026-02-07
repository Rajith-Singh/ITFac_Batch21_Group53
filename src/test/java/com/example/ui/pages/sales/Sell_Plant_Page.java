package com.example.ui.pages.sales;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class Sell_Plant_Page {

    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "plantId")
    private WebElement plantDropdown;

    @FindBy(id = "quantity")
    private WebElement quantityField;

    @FindBy(xpath = "//button[text()='Sell' or contains(@class, 'btn-primary') and contains(text(), 'Sell')]")
    private WebElement sellButton;

    @FindBy(xpath = "//div[contains(@class, 'alert-success')]")
    private WebElement successMessage;

    @FindBy(css = "div.alert.alert-danger")
    private WebElement errorAlertContainer;

    @FindBy(xpath = "//div[contains(@class, 'alert-danger')]//span")
    private WebElement errorAlertText;

    // Remove unused sellPlantLink field - it's not needed

    public Sell_Plant_Page(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isSellPlantPageDisplayed() {
        try {
            return plantDropdown.isDisplayed() &&
                    quantityField.isDisplayed() &&
                    sellButton.isDisplayed();
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("/ui/sales/new");
        }
    }

    // NEW METHOD: Print all available plant options
    public void printAvailablePlants() {
        try {
            wait.until(ExpectedConditions.visibilityOf(plantDropdown));
            Select plantSelect = new Select(plantDropdown);
            List<WebElement> options = plantSelect.getOptions();

            System.out.println("=== AVAILABLE PLANT OPTIONS ===");
            System.out.println("Total options: " + options.size());
            for (int i = 0; i < options.size(); i++) {
                WebElement option = options.get(i);
                System.out.println(i + ": " + option.getText() + " (value: " + option.getAttribute("value") + ")");
            }
            System.out.println("=============================");
        } catch (Exception e) {
            System.out.println("Error getting plant options: " + e.getMessage());
        }
    }

    // NEW METHOD: Get all plant options as List<String>
    public List<String> getAllPlantOptions() {
        List<String> plantOptions = new ArrayList<>();
        try {
            Select plantSelect = new Select(plantDropdown);
            List<WebElement> options = plantSelect.getOptions();
            for (WebElement option : options) {
                plantOptions.add(option.getText());
            }
        } catch (Exception e) {
            System.out.println("Error getting plant options: " + e.getMessage());
        }
        return plantOptions;
    }

    // UPDATED: Select plant by name (handles partial matches)
    public void selectPlantByName(String plantName) {
        wait.until(ExpectedConditions.visibilityOf(plantDropdown));
        Select plantSelect = new Select(plantDropdown);

        System.out.println("Looking for plant: " + plantName);

        // First try exact match
        try {
            plantSelect.selectByVisibleText(plantName);
            System.out.println("Selected plant by exact match: " + plantName);
            return;
        } catch (Exception e) {
            System.out.println("Exact match failed, trying partial match...");
        }

        // If exact match fails, try partial match
        List<WebElement> options = plantSelect.getOptions();
        for (WebElement option : options) {
            String optionText = option.getText();
            if (optionText.contains(plantName)) {
                plantSelect.selectByVisibleText(optionText);
                System.out.println("Selected plant by partial match: " + optionText);
                return;
            }
        }

        // If still not found, print available options and throw error
        System.out.println("Plant '" + plantName + "' not found. Available options:");
        for (WebElement option : options) {
            System.out.println("- " + option.getText());
        }
        throw new RuntimeException("Plant '" + plantName + "' not found in dropdown");
    }

    // NEW METHOD: Select plant by value
    public void selectPlantByValue(String value) {
        wait.until(ExpectedConditions.visibilityOf(plantDropdown));
        Select plantSelect = new Select(plantDropdown);
        plantSelect.selectByValue(value);
        System.out.println("Selected plant by value: " + value);
    }

    public String getSelectedPlant() {
        try {
            Select plantSelect = new Select(plantDropdown);
            return plantSelect.getFirstSelectedOption().getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void enterQuantity(String quantity) {
        wait.until(ExpectedConditions.visibilityOf(quantityField));
        quantityField.clear();
        quantityField.sendKeys(quantity);
    }

    public String getQuantityValue() {
        try {
            return quantityField.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public void clickSellButton() {
        wait.until(ExpectedConditions.elementToBeClickable(sellButton));
        sellButton.click();
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            // Wait a bit for message to appear
            Thread.sleep(1500);
            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMessageText() {
        try {
            return successMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    // SECOND TEST CASE METHODS (Updated to use PageFactory elements)

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return errorAlertContainer.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the text of the error message
     */
    public String getErrorMessageText() {
        try {
            return errorAlertText.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Alternative method to get error from alert using WebDriver (fallback)
     */
    public String getErrorAlertText() {
        try {
            List<WebElement> alerts = driver.findElements(By.cssSelector("div.alert.alert-danger"));
            if (!alerts.isEmpty()) {
                return alerts.get(0).getText().trim();
            }
        } catch (Exception e) {
            // Do nothing
        }
        return "";
    }

    /**
     * Check if specific error message is displayed
     */
    public boolean isSpecificErrorMessageDisplayed(String expectedError) {
        String actualError = getErrorMessageText();
        return actualError.contains(expectedError);
    }

    /**
     * Get the selected plant's stock information
     */
    public String getSelectedPlantStockInfo() {
        try {
            Select plantSelect = new Select(plantDropdown);
            WebElement selectedOption = plantSelect.getFirstSelectedOption();
            return selectedOption.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if plant dropdown is displayed
     */
    public boolean isPlantDropdownDisplayed() {
        try {
            return plantDropdown.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if quantity field is displayed
     */
    public boolean isQuantityFieldDisplayed() {
        try {
            return quantityField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if sell button is displayed
     */
    public boolean isSellButtonDisplayed() {
        try {
            return sellButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // REMOVED DUPLICATE getQuantityValue() method - keeping the one above

    /**
     * Clear the quantity field
     */
    public void clearQuantityField() {
        quantityField.clear();
    }

    /**
     * Select plant by exact option text (including stock)
     */
    public void selectPlantByExactText(String optionText) {
        Select plantSelect = new Select(plantDropdown);
        plantSelect.selectByVisibleText(optionText);
    }

    /**
     * Wait for error message to appear with specific text
     */
    public void waitForErrorMessage(String expectedErrorPart) {
        wait.until(ExpectedConditions.textToBePresentInElement(errorAlertText, expectedErrorPart));
    }

    /**
     * Check if error message disappears after some time
     */
    public boolean isErrorMessageGone() {
        try {
            wait.until(ExpectedConditions.invisibilityOf(errorAlertContainer));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Dismiss error alert if close button is present
     */
    public void dismissErrorAlert() {
        try {
            WebElement closeButton = driver.findElement(By.cssSelector("div.alert-danger button.btn-close"));
            if (closeButton.isDisplayed()) {
                closeButton.click();
            }
        } catch (Exception e) {
            // No close button or already dismissed
        }
    }

    /**
     * Get the selected option value
     */
    public String getSelectedPlantValue() {
        try {
            Select plantSelect = new Select(plantDropdown);
            return plantSelect.getFirstSelectedOption().getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Verify plant is selected
     */
    public boolean isPlantSelected(String plantName) {
        String selectedText = getSelectedPlant();
        return selectedText.contains(plantName);
    }

    /**
     * Get max stock from selected plant option
     */
    public int getSelectedPlantStock() {
        try {
            String selectedText = getSelectedPlant();
            // Extract stock number from text like "Peace Lily (Stock: 5)"
            if (selectedText.contains("Stock:")) {
                String stockPart = selectedText.split("Stock:")[1].trim();
                stockPart = stockPart.replace(")", "").trim();
                return Integer.parseInt(stockPart);
            }
        } catch (Exception e) {
            System.out.println("Error extracting stock: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Check if quantity field has max attribute
     */
    public boolean hasMaxQuantityAttribute() {
        String maxAttr = quantityField.getAttribute("max");
        return maxAttr != null && !maxAttr.isEmpty();
    }

    /**
     * Get max allowed quantity from input field
     */
    public int getMaxQuantity() {
        try {
            String maxAttr = quantityField.getAttribute("max");
            return maxAttr != null ? Integer.parseInt(maxAttr) : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Get min allowed quantity from input field
     */
    public int getMinQuantity() {
        try {
            String minAttr = quantityField.getAttribute("min");
            return minAttr != null ? Integer.parseInt(minAttr) : 1;
        } catch (Exception e) {
            return 1;
        }
    }
}