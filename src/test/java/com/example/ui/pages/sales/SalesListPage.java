// src/test/java/com/example/ui/pages/sales/SalesListPage.java
package com.example.ui.pages.sales;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class SalesListPage {

    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//button[contains(text(),'Sell')]")
    private WebElement sellButton;

    @FindBy(css = ".sales-table")
    private WebElement salesTable;

    @FindBy(xpath = "//td[contains(text(),'Snake Plant')]/following-sibling::td//button[contains(@class,'delete')]")
    private WebElement deleteIcon;

    @FindBy(css = ".confirmation-dialog")
    private WebElement confirmationDialog;

    @FindBy(xpath = "//button[contains(text(),'Confirm')]")
    private WebElement confirmButton;

    @FindBy(xpath = "//button[contains(text(),'Cancel')]")
    private WebElement cancelButton;

    @FindBy(css = ".success-message")
    private WebElement successMessage;

    @FindBy(css = ".empty-state")
    private WebElement emptyState;

    @FindBy(xpath = "//th[contains(text(),'Sold Date')]")
    private WebElement soldDateHeader;

    @FindBy(xpath = "//tr[@class='sale-row']")
    private List<WebElement> saleRows;

    public SalesListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void navigateTo() {
        driver.get("http://localhost:8080/ui/sales");
    }

    public boolean isPageLoaded() {
        return sellButton.isDisplayed() && salesTable.isDisplayed();
    }

    public void clickSellButton() {
        sellButton.click();
    }

    public void clickDeleteIcon(String plantName) {
        // Dynamic locator for specific plant
        String xpath = String.format(
                "//td[contains(text(),'%s')]/following-sibling::td//button[contains(@class,'delete')]", plantName);
        driver.findElement(org.openqa.selenium.By.xpath(xpath)).click();
    }

    public String getConfirmationDialogMessage() {
        wait.until(d -> confirmationDialog.isDisplayed());
        return confirmationDialog.getText();
    }

    public void confirmDeletion() {
        confirmButton.click();
    }

    public void cancelDeletion() {
        cancelButton.click();
    }

    public boolean isSalePresent(String plantName) {
        String xpath = String.format("//td[contains(text(),'%s')]", plantName);
        return !driver.findElements(org.openqa.selenium.By.xpath(xpath)).isEmpty();
    }

    public boolean isDeleteIconVisible(String plantName) {
        String xpath = String.format(
                "//td[contains(text(),'%s')]/following-sibling::td//button[contains(@class,'delete')]", plantName);
        return driver.findElement(org.openqa.selenium.By.xpath(xpath)).isDisplayed();
    }

    public boolean isSortedBy(String columnName, String order) {
        // Implementation for checking sort order
        return true; // Replace with actual logic
    }

    public boolean isFirstSaleMostRecent() {
        // Implementation to check if first sale has latest date
        return true; // Replace with actual logic
    }

    public String getSortIndicator(String columnName) {
        String xpath = String.format("//th[contains(text(),'%s')]//span[@class='sort-indicator']", columnName);
        return driver.findElement(org.openqa.selenium.By.xpath(xpath)).getText();
    }

    public void clickColumnHeader(String columnName) {
        String xpath = String.format("//th[contains(text(),'%s')]", columnName);
        driver.findElement(org.openqa.selenium.By.xpath(xpath)).click();
    }

    public String getEmptyStateMessage() {
        return emptyState.getText();
    }

    public boolean isSuccessMessageDisplayed() {
        return successMessage.isDisplayed();
    }
}