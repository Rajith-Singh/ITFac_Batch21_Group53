package com.example.ui.stepDefinitions.plants;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import com.example.utils.DriverManager;
import com.example.ui.pages.plants.AddEditPlantPage;

public class PlantCRUDSteps {
    private WebDriver driver;
    private AddEditPlantPage addEditPlantPage;

    public PlantCRUDSteps() {
        this.driver = DriverManager.getDriver();
        this.addEditPlantPage = new AddEditPlantPage(driver);
    }

    @When("user enters plant name as {string}")
    public void userEntersPlantNameAs(String plantName) {
        addEditPlantPage = new AddEditPlantPage(driver);
        addEditPlantPage.enterPlantName(plantName);
    }

    @When("user selects sub category as {string}")
    public void userSelectsSubCategoryAs(String categoryName) {
        addEditPlantPage = new AddEditPlantPage(driver);
        addEditPlantPage.selectCategory(categoryName);
    }

    @When("user enters price as {string}")
    public void userEntersPriceAs(String price) {
        addEditPlantPage = new AddEditPlantPage(driver);
        addEditPlantPage.enterPrice(price);
    }

    @When("user enters quantity as {string}")
    public void userEntersQuantityAs(String quantity) {
        addEditPlantPage = new AddEditPlantPage(driver);
        addEditPlantPage.enterQuantity(quantity);
    }

    @Then("a success message {string} should be displayed")
    public void aSuccessMessageShouldBeDisplayed(String expectedMessage) {
        addEditPlantPage = new AddEditPlantPage(driver);
        String actualMessage = addEditPlantPage.getSuccessMessage();
        String pageSource = driver.getPageSource();
        
        // Debug output to identify actual message structure
        System.out.println("\n===== DEBUG: Success Message Check =====");
        System.out.println("Expected Message: " + expectedMessage);
        System.out.println("Actual Message: " + actualMessage);
        System.out.println("Page URL: " + driver.getCurrentUrl());
        System.out.println("========================================\n");
        
        assert addEditPlantPage.isSuccessMessageDisplayed(expectedMessage) : 
            "Success message is not displayed. Actual message: " + actualMessage;
    }

    @Then("newly added plant {string} should be displayed in the plants list")
    public void newlyAddedPlantShouldBeDisplayedInTheList(String plantName) {
        addEditPlantPage = new AddEditPlantPage(driver);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Navigate back to plants list if needed
        com.example.ui.pages.plants.PlantListPage plantListPage = new com.example.ui.pages.plants.PlantListPage(driver);
        assert plantListPage.isPlantDisplayedInList(plantName) : "Plant '" + plantName + "' is not displayed in the list";
    }
}
