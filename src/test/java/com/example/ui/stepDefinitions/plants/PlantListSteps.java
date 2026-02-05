package com.example.ui.stepDefinitions.plants;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import com.example.utils.DriverManager;
import com.example.ui.pages.plants.PlantListPage;
import com.example.ui.pages.plants.AddEditPlantPage;

public class PlantListSteps {
    private WebDriver driver;
    private PlantListPage plantListPage;
    private AddEditPlantPage addEditPlantPage;

    public PlantListSteps() {
        this.driver = DriverManager.getDriver();
        this.plantListPage = new PlantListPage(driver);
        this.addEditPlantPage = new AddEditPlantPage(driver);
    }

    @When("user clicks on {string} button")
    public void userClicksOnButton(String buttonText) {
        if (buttonText.contains("Add a Plant")) {
            plantListPage = new PlantListPage(driver);
            plantListPage.clickAddPlantButton();
        } else if (buttonText.equalsIgnoreCase("save")) {
            addEditPlantPage = new AddEditPlantPage(driver);
            addEditPlantPage.clickSaveButton();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("user should be navigated to {string} page")
    public void userShouldBeNavigatedToPage(String pageName) {
        String currentUrl = driver.getCurrentUrl();
        if (pageName.contains("Add Plants")) {
            assert currentUrl.contains("/ui/plants/add") : "User is not on the Add Plants page. Current URL: " + currentUrl;
        }
    }

    @Then("user should be redirected to {string} page")
    public void userShouldBeRedirectedToPage(String pageUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String currentUrl = driver.getCurrentUrl();
        if (pageUrl.equals("/ui/plants")) {
            plantListPage = new PlantListPage(driver);
            assert plantListPage.isPlantListPageDisplayed() : "Plants list page is not displayed";
        }
    }
}
