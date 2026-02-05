package com.example.ui.stepDefinitions.sales;

import com.example.ui.pages.sales.SalesListPage;
import com.example.ui.pages.sales.SellPlantPage;
import com.example.utils.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class SellPlantSteps {
    private WebDriver driver;
    private SalesListPage salesListPage;
    private SellPlantPage sellPlantPage;

    public SellPlantSteps() {
        this.driver = DriverManager.getDriver();
        this.salesListPage = new SalesListPage(driver);
        this.sellPlantPage = new SellPlantPage(driver);
    }



    @Then("URL should change to {string}")
    public void urlShouldChangeTo(String expectedUrl) {
        String actualUrl = sellPlantPage.getCurrentUrl();
        System.out.println("Expected URL: " + expectedUrl);
        System.out.println("Actual URL: " + actualUrl);
        
        Assert.assertTrue(actualUrl.contains(expectedUrl), 
            "URL should contain " + expectedUrl + ". Actual: " + actualUrl);
    }

    @Then("Sell Plant form should load successfully")
    public void sellPlantFormShouldLoadSuccessfully() {
        // Verify the sell plant page is loaded
        Assert.assertTrue(sellPlantPage.isSellPlantPageLoaded(), 
            "Sell Plant page should be loaded");
        
        // Verify the form is loaded
        Assert.assertTrue(sellPlantPage.isSellPlantFormLoaded(), 
            "Sell Plant form should be loaded");
        
        // Wait for page to fully load
        sellPlantPage.waitForPageLoad();
    }

    @And("URL should contain {string}")
    public void urlShouldContain(String expectedPath) {
        String actualUrl = sellPlantPage.getCurrentUrl();
        Assert.assertTrue(actualUrl.contains(expectedPath), 
            "URL should contain " + expectedPath + ". Actual: " + actualUrl);
    }
}
