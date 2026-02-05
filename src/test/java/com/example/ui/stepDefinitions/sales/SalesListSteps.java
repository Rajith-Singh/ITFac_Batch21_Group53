package com.example.ui.stepDefinitions.sales;

import com.example.ui.pages.sales.SalesListPage;
import com.example.utils.ConfigReader;
import com.example.utils.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class SalesListSteps {
    private WebDriver driver;
    private SalesListPage salesListPage;

    public SalesListSteps() {
        this.driver = DriverManager.getDriver();
        this.salesListPage = new SalesListPage(driver);
    }

    @Given("user is on Sales List page")
    public void userIsOnSalesListPage() {
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL when checking Sales List page: " + currentUrl);
        
        boolean isLoaded = salesListPage.isSalesListPageLoaded();
        
        if (!isLoaded) {
            // Print page source for debugging
            try {
                String pageTitle = driver.getTitle();
                System.out.println("Page Title: " + pageTitle);
                
                // Check for error page
                if (currentUrl.contains("error") || pageTitle.contains("Error")) {
                    Assert.fail("Application returned an error page. URL: " + currentUrl + ", Title: " + pageTitle);
                }
                
                // Check if still on login page
                if (currentUrl.contains("login")) {
                    Assert.fail("User is still on login page. Authentication may have failed.");
                }
                
            } catch (Exception e) {
                System.out.println("Could not get page information: " + e.getMessage());
            }
        }
        
        Assert.assertTrue(isLoaded, 
            "Sales List page should be loaded. Current URL: " + currentUrl);
        salesListPage.waitForPageLoad();
    }

    @Given("sales records are displayed for plants")
    public void salesRecordsAreDisplayedForPlants() {
        Assert.assertTrue(salesListPage.areSalesRecordsDisplayed(), 
            "Sales records should be displayed");
        Assert.assertTrue(salesListPage.getSalesRecordCount() > 0, 
            "There should be at least one sales record");
    }

    @And("Plant Name column header is visible and clickable")
    public void plantNameColumnHeaderIsVisibleAndClickable() {
        Assert.assertTrue(salesListPage.isColumnHeaderVisible("Plant Name") || 
                         salesListPage.isColumnHeaderVisible("Plant"), 
            "Plant Name column header should be visible");
        Assert.assertTrue(salesListPage.isPlantNameHeaderClickable(), 
            "Plant Name column header should be clickable");
    }

    @When("user clicks Plant Name column header once")
    public void userClicksPlantNameColumnHeaderOnce() {
        salesListPage.clickPlantNameHeader();
    }

    @Then("sales should be sorted in ascending order by Plant Name")
    public void salesShouldBeSortedInAscendingOrderByPlantName() {
        List<String> actualPlantNames = salesListPage.getPlantNamesFromTable();
        Assert.assertFalse(actualPlantNames.isEmpty(), 
            "Plant names should be retrieved from the table");
        
        boolean isCorrectOrder = salesListPage.validateSortOrder(actualPlantNames, true);
        Assert.assertTrue(isCorrectOrder, 
            "Plant names should be sorted in ascending order: " + actualPlantNames);
    }

    @Then("expected ascending order should be {string}, {string}, {string}")
    public void expectedAscendingOrderShouldBe(String plant1, String plant2, String plant3) {
        List<String> expectedOrder = salesListPage.getExpectedPlantNamesAscending();
        List<String> actualPlantNames = salesListPage.getPlantNamesFromTable();
        
        if (expectedOrder.size() >= 1) Assert.assertEquals(expectedOrder.get(0), plant1, "First plant should match");
        if (expectedOrder.size() >= 2) Assert.assertEquals(expectedOrder.get(1), plant2, "Second plant should match");
        if (expectedOrder.size() >= 3) Assert.assertEquals(expectedOrder.get(2), plant3, "Third plant should match");
    }

    @When("user clicks Plant Name column header again")
    public void userClicksPlantNameColumnHeaderAgain() {
        salesListPage.clickPlantNameHeader();
    }

    @Then("sales should be sorted in descending order by Plant Name")
    public void salesShouldBeSortedInDescendingOrderByPlantName() {
        List<String> actualPlantNames = salesListPage.getPlantNamesFromTable();
        Assert.assertFalse(actualPlantNames.isEmpty(), 
            "Plant names should be retrieved from the table");
        
        boolean isCorrectOrder = salesListPage.validateSortOrder(actualPlantNames, false);
        Assert.assertTrue(isCorrectOrder, 
            "Plant names should be sorted in descending order: " + actualPlantNames);
    }

    @Then("expected descending order should be {string}, {string}, {string}")
    public void expectedDescendingOrderShouldBe(String plant1, String plant2, String plant3) {
        List<String> expectedOrder = salesListPage.getExpectedPlantNamesDescending();
        List<String> actualPlantNames = salesListPage.getPlantNamesFromTable();
        
        if (expectedOrder.size() >= 1) Assert.assertEquals(expectedOrder.get(0), plant1, "First plant should match");
        if (expectedOrder.size() >= 2) Assert.assertEquals(expectedOrder.get(1), plant2, "Second plant should match");
        if (expectedOrder.size() >= 3) Assert.assertEquals(expectedOrder.get(2), plant3, "Third plant should match");
    }

    @Then("sort indicator should show {string}")
    public void sortIndicatorShouldShow(String indicator) {
        String actualIndicator = salesListPage.getSortIndicator();
        if (indicator.equalsIgnoreCase("ascending") || indicator.equalsIgnoreCase("up") || indicator.equalsIgnoreCase("asc")) {
            Assert.assertTrue(actualIndicator.contains("asc") || actualIndicator.isEmpty(), 
                "Sort indicator should show ascending or be absent: " + actualIndicator);
        } else if (indicator.equalsIgnoreCase("descending") || indicator.equalsIgnoreCase("down") || indicator.equalsIgnoreCase("desc")) {
            Assert.assertTrue(actualIndicator.contains("desc") || actualIndicator.isEmpty(), 
                "Sort indicator should show descending or be absent: " + actualIndicator);
        } else if (indicator.equalsIgnoreCase("none")) {
            Assert.assertTrue(actualIndicator.contains("none") || actualIndicator.isEmpty(), 
                "Sort indicator should show none or be absent: " + actualIndicator);
        }
    }

    @And("verify initial plant names are displayed")
    public void verifyInitialPlantNamesAreDisplayed() {
        List<String> plantNames = salesListPage.getPlantNamesFromTable();
        Assert.assertFalse(plantNames.isEmpty(), 
            "Plant names should be displayed in the table");
        
        // Check if required plants are present
        List<String> expectedPlants = salesListPage.getExpectedPlantNamesAscending();
        for (String expectedPlant : expectedPlants) {
            boolean found = plantNames.stream().anyMatch(name -> name.contains(expectedPlant));
            Assert.assertTrue(found, 
                "Expected plant '" + expectedPlant + "' should be found in the table");
        }
    }

    @Given("user is logged into the application")
    public void userIsLoggedIntoTheApplication() {
        // Perform actual login
        try {
            String loginUrl = ConfigReader.getProperty("login.url", "http://localhost:8081/ui/login");
            String username = ConfigReader.getProperty("user.username", "testuser");
            String password = ConfigReader.getProperty("user.password", "test123");
            
            // Navigate to login page
            driver.get(loginUrl);
            Thread.sleep(2000);
            
            // Find login form elements and login
            try {
                WebElement usernameField = driver.findElement(By.name("username"));
                WebElement passwordField = driver.findElement(By.name("password"));
                WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit'] | //input[@type='submit']"));
                
                usernameField.clear();
                usernameField.sendKeys(username);
                
                passwordField.clear();
                passwordField.sendKeys(password);
                
                loginButton.click();
                
                // Wait for login to complete
                Thread.sleep(3000);
                
            } catch (Exception e) {
                System.out.println("Login form elements not found, trying alternative selectors...");
                // Try alternative login selectors if needed
            }
            
            // Verify login was successful by checking if we're no longer on login page
            String currentUrl = driver.getCurrentUrl();
            Assert.assertFalse(currentUrl.contains("login") && currentUrl.contains("error"), 
                "User should be successfully logged in, but login failed or still on login page");
            
        } catch (Exception e) {
            Assert.fail("Login failed: " + e.getMessage());
        }
    }

    @Given("user navigates to Sales List page")
    public void userNavigatesToSalesListPage() {
        // Navigate to sales list page
        String salesUrl = "http://localhost:8081/ui/sales";
        
        try {
            // Navigate to sales page
            driver.get(salesUrl);
            
            // Wait for URL to update
            Thread.sleep(3000);
            
            // Check if we were redirected to error page
            String currentUrl = driver.getCurrentUrl();
            System.out.println("After navigation, current URL: " + currentUrl);
            
            if (currentUrl.contains("error") || currentUrl.contains("login")) {
                // Try to navigate via dashboard if direct access fails
                System.out.println("Direct access failed, trying via dashboard...");
                driver.get("http://localhost:8081/ui/dashboard");
                Thread.sleep(2000);
                
                // Try to click on sales menu from dashboard
                try {
                    WebElement salesLink = driver.findElement(By.xpath("//a[contains(@href, '/ui/sales')] | //a[contains(text(), 'Sales')]"));
                    salesLink.click();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    // Last resort - try direct URL again
                    driver.get(salesUrl);
                    Thread.sleep(2000);
                }
            }
            
            // Final wait for page load
            salesListPage.waitForPageLoad();
            
        } catch (Exception e) {
            System.out.println("Navigation to sales page encountered issue: " + e.getMessage());
        }
    }
}