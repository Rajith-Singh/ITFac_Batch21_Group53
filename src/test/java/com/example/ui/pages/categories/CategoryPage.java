package com.example.ui.pages.categories;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CategoryPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators (Private as per Encapsulation)
 private By addCategoryButton = By.linkText("Add A Category"); // Replace with actual ID
 private By categoryTable = By.xpath("/html/body/div[1]/div/div[2]/div[2]/table");       // Replace with actual ID
 private By categoriesMenuLink = By.xpath("/html/body/div/div/div[1]/a[2]"); // Categories menu link

    
    // Constructor
    public CategoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Explicit Wait (Lecture 13)
    }

    // Actions
    public void clickCategoriesMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(categoriesMenuLink)).click();
    }

    public void clickAddCategory() {
        wait.until(ExpectedConditions.elementToBeClickable(addCategoryButton)).click();
    }

    public boolean isCategoryVisible(String categoryName) {
        // Validating result using dynamic XPath (Lecture 13 Best Practice)
        By categoryLocator = By.xpath("//td[text()='" + categoryName + "']");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(categoryLocator)).isDisplayed();
    }
}