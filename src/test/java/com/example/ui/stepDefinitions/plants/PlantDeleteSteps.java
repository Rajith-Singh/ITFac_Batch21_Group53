package com.example.ui.stepDefinitions.plants;

import com.example.ui.pages.plants.PlantListPage;
import com.example.ui.pages.plants.AddEditPlantPage;
import com.example.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class PlantDeleteSteps {
    private WebDriver driver;
    private PlantListPage plantListPage;
    private AddEditPlantPage addEditPlantPage;

    public PlantDeleteSteps() {
        this.driver = DriverManager.getDriver();
        this.plantListPage = new PlantListPage(driver);
        this.addEditPlantPage = new AddEditPlantPage(driver);
    }

    @Given("a plant {string} exists in the list")
    public void aPlantExistsInTheList(String plantName) {
        // First check if the plant already exists
        if (!plantListPage.isPlantDisplayedInList(plantName)) {
            // If not, create it
            plantListPage.clickAddPlantButton();

            // Fill in the form with test data
            addEditPlantPage.enterPlantName(plantName);
            addEditPlantPage.selectCategory("Rose UK");
            addEditPlantPage.enterPrice("1500");
            addEditPlantPage.enterQuantity("10");
            addEditPlantPage.clickSaveButton();

        }

        // Verify the plant is in the list
        Assert.assertTrue(
                plantListPage.isPlantDisplayedInList(plantName),
                "Plant '" + plantName + "' should be displayed in the list");
    }

    @When("user clicks delete button for plant {string}")
    public void userClicksDeleteButtonForPlant(String plantName) {
        System.out.println("=== Deleting Plant: " + plantName + " ===");
        plantListPage.clickDeleteButtonForPlant(plantName);
    }

    @When("user confirms the delete action")
    public void userConfirmsTheDeleteAction() {
        System.out.println("=== Confirming Delete Action ===");
        plantListPage.confirmDeleteAction();

    }

    @Then("the deleted plant {string} should no longer be visible in the plants list")
    public void deletedPlantShouldNotBeVisibleInList(String plantName) {
        System.out.println("=== Verifying Plant Deletion: " + plantName + " ===");
        boolean isDeleted = plantListPage.isPlantDeletedFromList(plantName);
        Assert.assertTrue(
                isDeleted,
                "Plant '" + plantName + "' should no longer be visible in the plants list");
    }
}
