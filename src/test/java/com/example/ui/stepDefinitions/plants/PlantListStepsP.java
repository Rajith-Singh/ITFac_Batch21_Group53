package com.example.ui.stepDefinitions.plants;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import com.example.utils.DriverManagerP;
import com.example.ui.pages.plants.PlantListPageP;
import com.example.ui.pages.plants.AddEditPlantPage;

public class PlantListStepsP {
    private WebDriver driver;
    private PlantListPageP plantListPage;
    private AddEditPlantPage addEditPlantPage;

    public PlantListStepsP() {
        // Constructor should not access driver as it might not be initialized yet
    }

    @When("user clicks on {string} button")
    public void userClicksOnButton(String buttonText) {
        this.driver = DriverManagerP.getDriver();
        if (buttonText.contains("Add a Plant")) {
            plantListPage = new PlantListPageP(driver);
            plantListPage.clickAddPlantButton();
        } else if (buttonText.equalsIgnoreCase("save")) {
            addEditPlantPage = new AddEditPlantPage(driver);
            addEditPlantPage.clickSaveButton();
        } else if (buttonText.equalsIgnoreCase("Plants")) {
            // Click sidebar link
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(org.openqa.selenium.By
                    .xpath("//div[contains(@class,'sidebar')]//a[contains(@href, '/ui/plants')]"))).click();
        } else if (buttonText.equalsIgnoreCase("Search")) {
            plantListPage = new PlantListPageP(driver);
            plantListPage.clickSearchButton();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("user should be navigated to {string} page")
    public void userShouldBeNavigatedToPage(String pageName) {
        this.driver = DriverManagerP.getDriver();
        String currentUrl = driver.getCurrentUrl();
        if (pageName.contains("Add Plants")) {
            assert currentUrl.contains("/ui/plants/add")
                    : "User is not on the Add Plants page. Current URL: " + currentUrl;
        }
    }

    @Then("user should be redirected to {string} page")
    public void userShouldBeRedirectedToPage(String pageUrl) {
        this.driver = DriverManagerP.getDriver();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        if (pageUrl.equals("/ui/plants")) {
            // Wait until URL actually changes to plants list
            wait.until(ExpectedConditions.urlContains("/ui/plants"));

            plantListPage = new PlantListPageP(driver);
            assert plantListPage.isPlantListPageDisplayed() : "Plants list page did not load after redirect!";
        }
    }

    @Then("I should see a plant with quantity less than {int} displaying a {string} badge")
    public void verifyLowStockBadge(int quantity, String badgeText) {
        this.driver = DriverManagerP.getDriver();
        plantListPage = new PlantListPageP(driver);
        // Find a plant with quantity < 5
        String plantName = plantListPage.getPlantNameWithQuantityLessThan(quantity);

        if (plantName != null) {
            boolean isBadgeDisplayed = plantListPage.isLowStockBadgeDisplayedForPlant(plantName);
            assert isBadgeDisplayed : "Expected 'Low' badge for plant '" + plantName + "' but it was not displayed.";
        } else {
            System.out.println("No plant with quantity less than " + quantity + " found. Skipping badge check.");
        }
    }

    @Then("I should see a plant with quantity {int} or more not displaying a {string} badge")
    public void verifyNoLowStockBadge(int quantity, String badgeText) {
        this.driver = DriverManagerP.getDriver();
        plantListPage = new PlantListPageP(driver);
        // Find a plant with quantity >= 5
        String plantName = plantListPage.getPlantNameWithQuantityGreaterThanOrEqualTo(quantity);

        if (plantName != null) {
            boolean isBadgeDisplayed = plantListPage.isLowStockBadgeDisplayedForPlant(plantName);
            assert !isBadgeDisplayed : "Did not expect 'Low' badge for plant '" + plantName + "' but it was displayed.";
        } else {
            System.out.println(
                    "No plant with quantity greater than or equal to " + quantity + " found. Skipping no badge check.");
        }
    }

    @When("user sorts the list by {string}")
    public void userSortsTheListBy(String column) {
        this.driver = DriverManagerP.getDriver();
        plantListPage = new PlantListPageP(driver);
        plantListPage.clickSortByColumn(column);
        // If sorting twice for Descending is needed, logic might need to be more
        // complex or feature file explicit
        // For now, assuming clicking once toggles or sets to some state.
        // Typically: Click once -> Asc, Click again -> Desc.
        // But the test step just says "sorts by <Column>".
        // The verification step handles the "Ascending" vs "Descending" check.
        // If the test requires specific click sequences, we might need to adjust.
        // Let's assume for this test, we just click. If "Descending" is expected, maybe
        // we need to click twice if it defaults to Asc?
        // But the scenario outline splits them.
        // If the table implementation toggles, then for "Descending", we might need to
        // know current state or click twice.
        // However, I will implement simple click for now. If verification fails, I'll
        // adjust.
    }

    @Then("the list should be sorted by {string} in {string} order")
    public void theListShouldBeSortedByInOrder(String column, String order) {
        this.driver = DriverManagerP.getDriver();
        plantListPage = new PlantListPageP(driver);

        boolean isSorted = false;
        if (column.equalsIgnoreCase("Name")) {
            java.util.List<String> names = plantListPage.getPlantNames();
            isSorted = isSortedString(names, order);
        } else if (column.equalsIgnoreCase("Price")) {
            java.util.List<Double> prices = plantListPage.getPlantPrices();
            isSorted = isSortedDouble(prices, order);
        } else if (column.equalsIgnoreCase("Quantity")) {
            java.util.List<Integer> quantities = plantListPage.getPlantQuantities();
            isSorted = isSortedInteger(quantities, order);
        }

        assert isSorted : "List is NOT sorted by " + column + " in " + order + " order.";
    }

    private boolean isSortedString(java.util.List<String> list, String order) {
        if (list.isEmpty() || list.size() == 1)
            return true;
        java.util.List<String> sorted = new java.util.ArrayList<>(list);
        if (order.equalsIgnoreCase("Ascending")) {
            java.util.Collections.sort(sorted, String.CASE_INSENSITIVE_ORDER);
        } else {
            java.util.Collections.sort(sorted, java.util.Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
        }
        return list.equals(sorted);
    }

    private boolean isSortedDouble(java.util.List<Double> list, String order) {
        if (list.isEmpty() || list.size() == 1)
            return true;
        java.util.List<Double> sorted = new java.util.ArrayList<>(list);
        if (order.equalsIgnoreCase("Ascending")) {
            java.util.Collections.sort(sorted);
        } else {
            java.util.Collections.sort(sorted, java.util.Collections.reverseOrder());
        }
        return list.equals(sorted);
    }

    private boolean isSortedInteger(java.util.List<Integer> list, String order) {
        if (list.isEmpty() || list.size() == 1)
            return true;
        java.util.List<Integer> sorted = new java.util.ArrayList<>(list);
        if (order.equalsIgnoreCase("Ascending")) {
            java.util.Collections.sort(sorted);
        } else {
            java.util.Collections.sort(sorted, java.util.Collections.reverseOrder());
        }
        return list.equals(sorted);
    }

    @When("user selects {string} from category dropdown")
    public void userSelectsFromCategoryDropdown(String categoryName) {
        this.driver = DriverManagerP.getDriver();
        plantListPage = new PlantListPageP(driver);
        plantListPage.selectCategory(categoryName);
    }

    @Then("only plants with category {string} should be displayed")
    public void onlyPlantsWithCategoryShouldBeDisplayed(String expectedCategory) {
        this.driver = DriverManagerP.getDriver();
        plantListPage = new PlantListPageP(driver);

        java.util.List<String> displayedCategories = plantListPage.getPlantCategories();

        for (String category : displayedCategories) {
            assert category.equalsIgnoreCase(expectedCategory)
                    : "Expected only '" + expectedCategory + "' plants, but found: " + category;
        }

        assert !displayedCategories.isEmpty()
                : "No plants displayed after filtering by category: " + expectedCategory;
    }

    @When("user enters {string} in the search field")
    public void userEntersInTheSearchField(String searchText) {
        this.driver = DriverManagerP.getDriver();
        plantListPage = new PlantListPageP(driver);
        plantListPage.enterSearchText(searchText);
    }

    @Then("the plant list should display plants matching {string}")
    public void thePlantListShouldDisplayPlantsMatching(String searchTerm) {
        this.driver = DriverManagerP.getDriver();
        plantListPage = new PlantListPageP(driver);

        java.util.List<String> displayedPlants = plantListPage.getPlantNames();

        assert !displayedPlants.isEmpty()
                : "No plants displayed after searching for: " + searchTerm;

        for (String plantName : displayedPlants) {
            assert plantName.toLowerCase().contains(searchTerm.toLowerCase())
                    : "Plant '" + plantName + "' does not match search term: " + searchTerm;
        }
    }
}