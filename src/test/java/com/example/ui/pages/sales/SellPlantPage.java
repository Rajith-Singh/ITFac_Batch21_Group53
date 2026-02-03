// src/test/java/com/example/ui/pages/sales/SellPlantPage.java
package com.example.ui.pages.sales;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class SellPlantPage {

    private WebDriver driver;

    @FindBy(id = "plant")
    private WebElement plantDropdown;

    @FindBy(id = "quantity")
    private WebElement quantityField;

    @FindBy(css = "button[type='submit']")
    private WebElement sellButton;

    @FindBy(css = ".error-message")
    private WebElement errorMessage;

    @FindBy(css = ".success-message")
    private WebElement successMessage;

    @FindBy(xpath = "//button[contains(text(),'Cancel')]")
    private WebElement cancelButton;

    public SellPlantPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageLoaded() {
        return plantDropdown.isDisplayed() && quantityField.isDisplayed();
    }

    public void selectPlant(String plantName) {
        Select dropdown = new Select(plantDropdown);
        dropdown.selectByVisibleText(plantName);
    }

    public void enterQuantity(String quantity) {
        quantityField.clear();
        quantityField.sendKeys(quantity);
    }

    public void clickSellButton() {
        sellButton.click();
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }

    public String getSuccessMessage() {
        return successMessage.getText();
    }

    public void clickCancelButton() {
        cancelButton.click();
    }
}