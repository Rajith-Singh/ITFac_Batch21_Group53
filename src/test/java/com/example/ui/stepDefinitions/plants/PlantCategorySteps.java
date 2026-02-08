package com.example.ui.stepDefinitions.plants;

import com.example.ui.pages.plants.AddEditPlantPage;
import com.example.utils.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PlantCategorySteps {
    private WebDriver driver = DriverManager.getDriver();
    private AddEditPlantPage addEditPlantPage = new AddEditPlantPage(driver);

    @And("the category dropdown should contains sub-categories {string}")
    public void categoryDropdownShouldContainSubCategories(String subCategories) {
        List<String> expectedSubCategories = Arrays.stream(subCategories.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        List<String> actualOptions = addEditPlantPage.getAvailableCategories();

        System.out.println("Expected Sub-Categories: " + expectedSubCategories);
        System.out.println("Actual Options in Dropdown: " + actualOptions);

        for (String expected : expectedSubCategories) {
            Assert.assertTrue(actualOptions.contains(expected),
                    "Expected sub-category '" + expected + "' not found in dropdown options: " + actualOptions);
        }
    }

    @And("the category dropdown should not contain main categories {string}")
    public void categoryDropdownShouldNotContainMainCategories(String mainCategories) {
        List<String> prohibitedMainCategories = Arrays.stream(mainCategories.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        List<String> actualOptions = addEditPlantPage.getAvailableCategories();

        for (String prohibited : prohibitedMainCategories) {
            Assert.assertFalse(actualOptions.contains(prohibited),
                    "Main category '" + prohibited + "' should NOT be present in dropdown options, but was found!");
        }
    }
}
