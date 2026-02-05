package com.example.ui.pages.plants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class PlantListPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//a[contains(@href, '/ui/plants/add') and contains(text(), 'Add a Plant')]")
    private WebElement addPlantButton;

    @FindBy(xpath = "//table[@class='table table-striped table-bordered align-middle']")
    private WebElement plantsTable;

    public PlantListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void navigateToPlantsList(String baseUrl) {
        driver.navigate().to(baseUrl + "/ui/plants");
    }

    public void clickAddPlantButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addPlantButton)).click();
    }

    public boolean isPlantListPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(addPlantButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlantDisplayedInList(String plantName) {
        try {
            String xpath = "//table[@class='table table-striped table-bordered align-middle']//tbody//td[contains(text(), '" + plantName + "')]";
            WebElement plantRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            return plantRow.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void clickDeleteButtonForPlant(String plantName) {
        try {
            // XPath to find the delete button for a specific plant
            String xpath = "//td[contains(text(), '" + plantName + "')]//ancestor::tr//form[contains(@action, '/plants/delete')]//button[@title='Delete']";
            WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            deleteButton.click();
        } catch (Exception e) {
            System.out.println("Error clicking delete button for plant: " + plantName + " - " + e.getMessage());
            throw new RuntimeException("Could not click delete button for plant: " + plantName);
        }
    }

    public void confirmDeleteAction() {
        try {
            // Handle browser's native confirm dialog
            org.openqa.selenium.Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (Exception e) {
            System.out.println("Could not handle confirm dialog: " + e.getMessage());
            throw new RuntimeException("Failed to confirm delete action");
        }
    }

    public boolean isSuccessMessageDisplayed(String message) {
        try {
            String xpath = "//div[contains(@class, 'alert-success')]//span[contains(text(), '" + message + "')]";
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            return successMsg.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlantDeletedFromList(String plantName) {
        try {
            // Wait for the element to disappear (max 5 seconds)
            String xpath = "//table[@class='table table-striped table-bordered align-middle']//tbody//td[contains(text(), '" + plantName + "')]";
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.stalenessOf(driver.findElement(By.xpath(xpath))));
            return true;
        } catch (Exception e) {
            // Also try to check if element is not present at all
            try {
                String xpath = "//table[@class='table table-striped table-bordered align-middle']//tbody//td[contains(text(), '" + plantName + "')]";
                return driver.findElements(By.xpath(xpath)).isEmpty();
            } catch (Exception ex) {
                return true;
            }
        }
    }
}
