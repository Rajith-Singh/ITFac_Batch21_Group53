package com.example.ui.stepDefinitions.plants;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.testng.Assert.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
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

        // Debug output to identify actual message structure
        System.out.println("\n===== DEBUG: Success Message Check =====");
        System.out.println("Expected Message: " + expectedMessage);
        System.out.println("Actual Message: " + actualMessage);
        System.out.println("Page URL: " + driver.getCurrentUrl());
        System.out.println("========================================\n");

        assertTrue(addEditPlantPage.isSuccessMessageDisplayed(expectedMessage),
                "Success message is not displayed. Actual message: " + actualMessage);
    }

    @When("admin clicks on edit icon for plant {string}")
    public void adminClicksOnEditIconForPlant(String plantName) {
        com.example.ui.pages.plants.PlantListPage plantListPg = new com.example.ui.pages.plants.PlantListPage(driver);
        plantListPg.clickEditButtonForPlant(plantName);
    }

    @Then("plant {string} should be updated with category {string}, price {string}, and quantity {string}")
    public void plantShouldBeUpdatedWith(String plantName, String category, String price, String quantity) {
        // In a real scenario, we might need to refresh or wait
        // The page usually redirects back to the list after save

        boolean found = false;
        // Verify in the list
        java.util.List<WebElement> rows = driver.findElements(By.xpath("//table//tbody//tr"));
        for (WebElement row : rows) {
            String nameText = row.findElement(By.xpath("./td[1]")).getText().trim();
            if (nameText.equalsIgnoreCase(plantName)) {
                found = true;
                String actualCategory = row.findElement(By.xpath("./td[2]")).getText().trim();
                String actualPrice = row.findElement(By.xpath("./td[3]")).getText().trim()
                        .replaceAll("[^0-9]", "");
                String actualQuantity = row.findElement(By.xpath("./td[4]")).getText().trim()
                        .replaceAll("[^0-9]", "");

                assertEquals(actualCategory, category, "Category mismatch for plant: " + plantName);
                assertEquals(actualPrice, price, "Price mismatch for plant: " + plantName);
                assertEquals(actualQuantity, quantity, "Quantity mismatch for plant: " + plantName);
                break;
            }
        }
        assertTrue(found, "Plant " + plantName + " not found in the list after update");
    }

    @Then("newly added plant {string} should be displayed in the plants list")
    public void newlyAddedPlantShouldBeDisplayedInTheList(String plantName) {
        com.example.ui.pages.plants.PlantListPage plantListPage = new com.example.ui.pages.plants.PlantListPage(driver);
        assertTrue(plantListPage.isPlantDisplayedInList(plantName),
                "Newly added plant '" + plantName + "' is not displayed in the plants list.");
    }
}
