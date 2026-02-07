package com.example.ui.pages.categories;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AddCategoryPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
   private By nameField = By.id("name"); // Replace with Category Name field ID
   private By saveButton = By.xpath("/html/body/div/div/div[2]/div[2]/form/button");     // Replace with Save button ID
   private By successMessage = By.cssSelector(".alert-success");

    public AddCategoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterCategoryName(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField)).sendKeys(name);
    }

    public void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    }

    public boolean isSuccessMessageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
    }
}