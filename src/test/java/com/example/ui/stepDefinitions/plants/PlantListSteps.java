package com.example.ui.stepDefinitions.plants;

import com.example.ui.pages.plants.PlantListPage;
import com.example.utils.ConfigReader;
import com.example.utils.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class PlantListSteps {
    private WebDriver driver;
    private PlantListPage plantListPage;

    public PlantListSteps() {
        this.driver = DriverManager.getDriver();
        this.plantListPage = new PlantListPage(driver);
    }

    @Given("user navigates to Plants List page")
    public void userNavigatesToPlantsListPage() {
        // Navigate to plants list page
        String plantsUrl = "http://localhost:8081/ui/plants";
        
        try {
            // Navigate to plants page
            driver.get(plantsUrl);
            
            // Enhanced wait for navigation with fallback strategy
            try {
                // Wait for URL to contain plants path
                new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.urlContains("/ui/plants"));
                
                // Wait for page to be ready
                new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
                
                System.out.println("Navigation to plants list completed");
                
            } catch (Exception e) {
                System.out.println("Navigation wait timed out, using fallback wait...");
                Thread.sleep(5000);
            }
            
            // Check if we were redirected to error page
            String currentUrl = driver.getCurrentUrl();
            System.out.println("After navigation, current URL: " + currentUrl);
            
            if (currentUrl.contains("error") || currentUrl.contains("login")) {
                // Try to navigate via dashboard if direct access fails
                System.out.println("Direct access failed, trying via dashboard...");
                driver.get("http://localhost:8081/ui/dashboard");
                Thread.sleep(2000);
                
                // Try to click on plants menu from dashboard
                try {
                    WebElement plantsLink = driver.findElement(By.xpath("//a[contains(@href, '/ui/plants')] | //a[contains(text(), 'Plants')]"));
                    plantsLink.click();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    // Last resort - try direct URL again
                    driver.get(plantsUrl);
                    Thread.sleep(2000);
                }
            }
            
            // Final wait for page load
            plantListPage.waitForPageLoad();
            
        } catch (Exception e) {
            System.out.println("Navigation to plants page encountered issue: " + e.getMessage());
        }
    }

    @Given("user is on Plants List page")
    public void userIsOnPlantsListPage() {
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL when checking Plants List page: " + currentUrl);
        
        boolean isLoaded = plantListPage.isPlantsListPageLoaded();
        
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
            "Plants List page should be loaded. Current URL: " + currentUrl);
        plantListPage.waitForPageLoad();
    }

    @When("user manually navigates to the Admin-only URL {string}")
    public void userManuallyNavigatesToTheAdminOnlyURL(String adminUrl) {
        // Construct the full URL
        String fullUrl = "http://localhost:8081" + adminUrl;
        System.out.println("Attempting to access Admin-only URL: " + fullUrl);
        
        try {
            driver.get(fullUrl);
            Thread.sleep(3000); // Wait for redirection/error to occur
        } catch (Exception e) {
            System.out.println("Navigation to Admin-only URL encountered issue: " + e.getMessage());
        }
    }

    @Then("user should be restricted from accessing the page")
    public void userShouldBeRestrictedFromAccessingThePage() {
        // Check that user is restricted (not able to access the admin page)
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after restriction attempt: " + currentUrl);
        
        // Verify that we're not on the admin page
        Assert.assertFalse(currentUrl.contains("/ui/plants/add"), 
            "User should not be able to access the Admin-only Add Plant page");
    }

    @And("user should be redirected to either login page or error page")
    public void userShouldBeRedirectedToEitherLoginPageOrErrorPage() {
        String currentUrl = driver.getCurrentUrl();
        String pageTitle = "";
        
        try {
            pageTitle = driver.getTitle();
        } catch (Exception e) {
            // Continue if title cannot be retrieved
        }
        
        System.out.println("Checking redirection - URL: " + currentUrl + ", Title: " + pageTitle);
        
        // Check for login page redirection
        boolean isLoginPage = currentUrl.contains("login") || 
                             pageTitle.toLowerCase().contains("login") ||
                             currentUrl.contains("/ui/login");
        
        // Check for error page (403, access denied, etc.)
        boolean isErrorPage = currentUrl.contains("error") || 
                              currentUrl.contains("403") || 
                              currentUrl.contains("forbidden") ||
                              currentUrl.contains("denied") ||
                              pageTitle.toLowerCase().contains("error") ||
                              pageTitle.toLowerCase().contains("forbidden") ||
                              pageTitle.toLowerCase().contains("access denied");
        
        Assert.assertTrue(isLoginPage || isErrorPage, 
            "User should be redirected to login page or error page. Current URL: " + currentUrl);
    }

    @And("the URL should change to indicate restriction")
    public void theUrlShouldChangeToIndicateRestriction() {
        String currentUrl = driver.getCurrentUrl();
        
        // Verify that URL has changed from the admin URL
        Assert.assertFalse(currentUrl.contains("/ui/plants/add"), 
            "URL should have changed from the Admin-only page");
        
        // Verify that URL indicates some form of restriction
        boolean indicatesRestriction = currentUrl.contains("login") || 
                                       currentUrl.contains("error") || 
                                       currentUrl.contains("403") || 
                                       currentUrl.contains("forbidden") || 
                                       currentUrl.contains("denied");
        
        Assert.assertTrue(indicatesRestriction, 
            "URL should indicate restriction (login, error, 403, etc.). Current URL: " + currentUrl);
    }

    @And("a clear permission error should be displayed")
    public void aClearPermissionErrorShouldBeDisplayed() {
        // Check for error messages on the page
        boolean hasPermissionError = plantListPage.hasPermissionErrorMessage();
        
        Assert.assertTrue(hasPermissionError, 
            "A clear permission error message should be displayed");
    }

    @And("the system should show one of the following:")
    public void theSystemShouldShowOneOfTheFollowing(io.cucumber.datatable.DataTable dataTable) {
        List<List<String>> restrictionTypes = dataTable.asLists();
        String currentUrl = driver.getCurrentUrl();
        String pageTitle = "";
        
        try {
            pageTitle = driver.getTitle();
        } catch (Exception e) {
            // Continue if title cannot be retrieved
        }
        
        System.out.println("Checking restriction types from data table");
        
        boolean foundRestrictionType = false;
        
        for (List<String> row : restrictionTypes) {
            if (row.size() >= 2) {
                String restrictionType = row.get(0);
                String description = row.get(1);
                
                System.out.println("Checking restriction type: " + restrictionType + " - " + description);
                
                if (restrictionType.equalsIgnoreCase("Login Redirect")) {
                    if (currentUrl.contains("login") || pageTitle.toLowerCase().contains("login")) {
                        foundRestrictionType = true;
                        System.out.println("Found Login Redirect restriction");
                        break;
                    }
                } else if (restrictionType.equalsIgnoreCase("403 Forbidden")) {
                    if (currentUrl.contains("403") || currentUrl.contains("forbidden") || 
                        pageTitle.toLowerCase().contains("403") || pageTitle.toLowerCase().contains("forbidden")) {
                        foundRestrictionType = true;
                        System.out.println("Found 403 Forbidden restriction");
                        break;
                    }
                } else if (restrictionType.equalsIgnoreCase("Access Denied")) {
                    if (currentUrl.contains("denied") || pageTitle.toLowerCase().contains("access denied") ||
                        plantListPage.hasAccessDeniedMessage()) {
                        foundRestrictionType = true;
                        System.out.println("Found Access Denied restriction");
                        break;
                    }
                }
            }
        }
        
        Assert.assertTrue(foundRestrictionType, 
            "System should show one of the expected restriction types. URL: " + currentUrl + ", Title: " + pageTitle);
    }

    @And("an error message should be displayed on the screen")
    public void anErrorMessageShouldBeDisplayedOnTheScreen() {
        boolean hasErrorMessage = plantListPage.hasErrorMessage();
        
        Assert.assertTrue(hasErrorMessage, 
            "An error message should be displayed on the screen");
    }

    @And("the error message should indicate insufficient permissions")
    public void theErrorMessageShouldIndicateInsufficientPermissions() {
        String errorMessage = plantListPage.getErrorMessage();
        
        Assert.assertNotNull(errorMessage, "Error message should not be null");
        Assert.assertFalse(errorMessage.trim().isEmpty(), "Error message should not be empty");
        
        // Check for permission-related keywords
        String lowerCaseMessage = errorMessage.toLowerCase();
        boolean indicatesPermissionIssue = lowerCaseMessage.contains("permission") || 
                                          lowerCaseMessage.contains("access") || 
                                          lowerCaseMessage.contains("authorized") || 
                                          lowerCaseMessage.contains("forbidden") || 
                                          lowerCaseMessage.contains("denied") || 
                                          lowerCaseMessage.contains("admin") || 
                                          lowerCaseMessage.contains("privilege");
        
        Assert.assertTrue(indicatesPermissionIssue, 
            "Error message should indicate insufficient permissions. Message: " + errorMessage);
    }

    // TC_UI_PLANTS_USER_11 - Edit Plant page restriction step definitions
    @When("user manually navigates to the Edit Plant page {string}")
    public void userManuallyNavigatesToTheEditPlantPage(String editUrl) {
        // Construct the full URL
        String fullUrl = "http://localhost:8081" + editUrl;
        System.out.println("Attempting to access Edit Plant page: " + fullUrl);
        
        try {
            driver.get(fullUrl);
            Thread.sleep(3000); // Wait for redirection/error to occur
        } catch (Exception e) {
            System.out.println("Navigation to Edit Plant page encountered issue: " + e.getMessage());
        }
    }

    @Then("user should be restricted from accessing the Edit Plant page")
    public void userShouldBeRestrictedFromAccessingTheEditPlantPage() {
        // Check that user is restricted (not able to access the edit page)
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after restriction attempt: " + currentUrl);
        
        // Verify that we're not on the edit page
        Assert.assertFalse(currentUrl.contains("/ui/plants/edit/"), 
            "User should not be able to access the Edit Plant page");
    }

    @And("no edit form should be displayed to the user")
    public void noEditFormShouldBeDisplayedToTheUser() {
        // Check that no edit form is displayed
        boolean hasEditForm = plantListPage.hasEditForm();
        
        Assert.assertFalse(hasEditForm, 
            "No edit form should be displayed to the user");
    }

    @And("the edit form fields should not be accessible")
    public void theEditFormFieldsShouldNotBeAccessible() {
        // Check that edit form fields are not accessible
        boolean hasEditFields = plantListPage.hasEditFormFields();
        
        Assert.assertFalse(hasEditFields, 
            "Edit form fields should not be accessible");
    }

    @And("save/update buttons should not be available")
    public void saveUpdateButtonsShouldNotBeAvailable() {
        // Check that save/update buttons are not available
        boolean hasSaveButtons = plantListPage.hasSaveUpdateButtons();
        
        Assert.assertFalse(hasSaveButtons, 
            "Save/Update buttons should not be available");
    }

    @Then("save\\/update buttons should not be available")
    public void save_update_buttons_should_not_be_available() {
        // Check that save/update buttons are not available
        boolean hasSaveButtons = plantListPage.hasSaveUpdateButtons();
        
        Assert.assertFalse(hasSaveButtons, 
            "Save/Update buttons should not be available");
    }

    
}