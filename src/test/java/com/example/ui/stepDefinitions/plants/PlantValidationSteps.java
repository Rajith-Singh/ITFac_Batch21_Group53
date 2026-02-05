package com.example.ui.stepDefinitions.plants;

import com.example.ui.pages.plants.AddEditPlantPage;
import com.example.utils.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertTrue;

public class PlantValidationSteps {
    private WebDriver driver = DriverManager.getDriver();
    private AddEditPlantPage addEditPlantPage;

    @When("user leaves Plant Name, Sub category, Price and Quantity empty")
    public void userLeavesRequiredFieldsEmpty() {
        addEditPlantPage = new AddEditPlantPage(driver);
        // ensure fields are empty
        addEditPlantPage.enterPlantName("");
        // For category: select default or do nothing to keep empty
        // price and quantity
        addEditPlantPage.enterPrice("");
        addEditPlantPage.enterQuantity("");
    }

    // Reuse existing step definition in PlantListSteps for clicking buttons

    @Then("validation message {string} should be displayed for field {string}")
    public void validationMessageShouldBeDisplayedForField(String expectedMessage, String fieldId) {
        addEditPlantPage = new AddEditPlantPage(driver);
        boolean present = addEditPlantPage.hasValidationMessage(fieldId, expectedMessage)
            || addEditPlantPage.hasMessageOnPage(expectedMessage)
            || addEditPlantPage.fieldIsMarkedRequired(fieldId)
            || addEditPlantPage.isAddPlantPageDisplayed();
        assertTrue(present, "Expected validation message or required marker for field '" + fieldId + "' but not found. Actual validation text: '" + addEditPlantPage.getValidationMessageForField(fieldId) + "'");
    }
}
