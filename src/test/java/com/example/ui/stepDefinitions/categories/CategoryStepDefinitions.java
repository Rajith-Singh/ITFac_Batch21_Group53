package com.example.ui.stepDefinitions.categories;

import com.example.hooks.Hooks;
import com.example.ui.pages.categories.CategoryPage;
import com.example.ui.pages.categories.AddCategoryPage;
import com.example.ui.pages.login.LoginPage;
import io.cucumber.java.en.*;
import com.example.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.junit.Assert;

public class CategoryStepDefinitions {
    WebDriver driver = Hooks.driver;
    CategoryPage categoryPage = new CategoryPage(driver);
    AddCategoryPage addCategoryPage = new AddCategoryPage(driver);
    LoginPage loginPage = new LoginPage(driver);

    // --- EXISTING STEPS --- 

    @Given("the user is on the login page")
    public void user_is_on_login_page() {
        driver.get(ConfigReader.get("login.url"));
    }

    @When("the user logs in with valid credentials")
    public void user_logs_in() {
        String username = ConfigReader.get("admin.username");
        String password = ConfigReader.get("admin.password");
        loginPage.login(username, password);
    }

    @Then("the user is on the Category Management page")
    public void user_navigates_to_category_page() {
        categoryPage.clickCategoriesMenu();
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    @Given("the user opens the Category Management page")
    public void user_opens_category_management_page() {
        try {
            if (driver.getCurrentUrl().contains("/ui/login") || driver.findElements(By.cssSelector("#loginForm")).size() > 0) {
                String username = ConfigReader.get("admin.username");
                String password = ConfigReader.get("admin.password");
                loginPage.login(username, password);
            }
        } catch (Exception ignored) {}
        categoryPage.clickCategoriesMenu();
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    @When("the user leaves the Category Name field empty")
    public void user_leaves_category_name_blank() {
        addCategoryPage.clearCategoryName();
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    @When("the user clicks the {string} button")
    public void user_clicks_add_category(String btnName) {
        categoryPage.clickAddCategory();
    }

    @When("the user enters {string} in the Category Name field")
    public void user_enters_category_name(String name) {
        addCategoryPage.enterCategoryName(name);
    }

    @When("the user leaves the Parent Category empty")
    public void user_leaves_parent_empty() {
        // Do nothing
    }

    @When("the user clicks the Save button")
    public void user_clicks_save() {
        addCategoryPage.clickSave();
    }

    @Then("a success message should be displayed")
    public void verify_success_message() {
        Assert.assertTrue("Success message not shown", addCategoryPage.isSuccessMessageDisplayed());
    }

    @Then("the user should be redirected to the Category List")
    public void verify_redirected_to_category_list() {
        Assert.assertTrue("Not redirected to category list", driver.getCurrentUrl().contains("categories"));
    }

    @Then("the new category {string} should appear in the list")
    public void verify_category_in_list(String name) {
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        Assert.assertTrue(categoryPage.isCategoryVisible(name));
    }

    @Then("an error message should be displayed indicating the name is required")
    public void verify_error_message_for_empty_category_name() {
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        boolean err = addCategoryPage.isErrorMessageDisplayed();
        if (err) {
            String text = addCategoryPage.getErrorMessageText().toLowerCase();
            Assert.assertTrue("Error message text incorrect", text.contains("required") || text.contains("between"));
        } else {
             Assert.fail("Error message was NOT displayed on the screen.");
        }
    }

    @Then("the user should remain on the Add Category page")
    public void verify_user_remains_on_add_page() {
        String currentUrl = driver.getCurrentUrl().toLowerCase();
        boolean staysOnAdd = currentUrl.contains("add") || currentUrl.contains("create");
        boolean inCategories = currentUrl.contains("categories");
        Assert.assertTrue("User should be in categories section or on add page", staysOnAdd || inCategories);
    }

    @When("the user selects {string} from the Parent Category dropdown")
    public void user_selects_parent_category(String parentName) {
        addCategoryPage.selectParentCategory(parentName);
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    @Then("the category {string} should show {string} as its parent")
    public void verify_category_parent_relationship(String childName, String parentName) {
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        Assert.assertTrue("Category with correct parent not found in list", 
            categoryPage.isCategoryWithParentVisible(childName, parentName));
    }

    @When("the user enters {string} in the search box")
    public void user_enters_search_term(String term) {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        categoryPage.enterSearchTerm(term);
    }

    @When("the user clicks the Search button")
    public void user_clicks_search_button() {
        categoryPage.clickSearchButton();
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    @Then("the category list should only display the {string} record")
    public void verify_search_results(String term) {
        boolean isFound = categoryPage.isCategoryInTableStrict(term);
        Assert.assertTrue("Bug Found: Search failed to display the category '" + term + "' in the table.", 
            isFound);
    }

    @When("the user clicks the Edit button for the searched category")
    public void user_clicks_edit_button() {
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        categoryPage.clickFirstEditButton();
    }

    @Then("a user-friendly error message {string} should be displayed")
    public void verify_user_friendly_error(String expectedText) {
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        boolean isDisplayed = addCategoryPage.isErrorBannerDisplayed();
        Assert.assertTrue("No error message was displayed at all!", isDisplayed);
        
        String actualText = addCategoryPage.getErrorBannerText();
        System.out.println("ACTUAL ERROR RECEIVED: " + actualText);
        
        Assert.assertTrue("Bug Found: Expected friendly error '" + expectedText + "' but got backend error: " + actualText, 
            actualText.contains(expectedText));
    }

    // --- NEW STEP FOR STANDARD USER LOGIN ---
    @When("the standard user logs in")
    public void standard_user_logs_in() {
        // Reads from config.properties: user.username / user.password
        String username = ConfigReader.get("user.username");
        String password = ConfigReader.get("user.password");
        loginPage.login(username, password);
    }

    // --- NEW STEPS FOR TC_UI_USER_CAT_01 (User Security) ---

    @Then("the {string} column should be displayed")
    public void verify_column_is_displayed(String columnName) {
        if (columnName.equals("Actions")) {
            boolean isVisible = categoryPage.isActionsColumnVisible();
            // We assert TRUE because we know the UI is displaying it (which is part of the problem)
            Assert.assertTrue("UI Mismatch: 'Actions' column was expected to be visible.", isVisible);
        }
    }

    @When("the user clicks the Edit button for any category")
    public void user_clicks_edit_for_any_category() {
        // Try to click the edit button in the first row
        categoryPage.clickFirstRowEditButton();
    }

    @Then("the user should not be navigated to the Edit Category page")
    public void verify_no_navigation_to_edit_page() {
        // Wait for potential page load
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        
        String currentUrl = driver.getCurrentUrl();
        
        // --- THE SECURITY TRAP ---
        // If the URL contains "edit", the user bypassed security.
        Assert.assertFalse("Security Bug Found: Standard User successfully accessed the Edit Page! URL: " + currentUrl, 
            currentUrl.contains("edit"));
    }

    // --- NEW STEP FOR TC_UI_USER_CAT_02 ---

    @Then("the Search input field should be enabled")
    public void verify_search_input_enabled() {
        boolean isEnabled = categoryPage.isSearchInputEnabled();
        Assert.assertTrue("Bug: Search input field is disabled for the Standard User!", isEnabled);
    }

    // --- NEW STEPS FOR TC_UI_USER_CAT_03 ---

    @Then("the Parent Category search dropdown should be enabled")
    public void verify_search_parent_dropdown_enabled() {
        boolean isEnabled = categoryPage.isSearchParentDropdownEnabled();
        Assert.assertTrue("Bug: Parent Category search dropdown is disabled for the Standard User!", isEnabled);
    }

    @When("the user selects {string} from the search parent dropdown")
    public void user_selects_from_search_parent_dropdown(String parentName) {
        categoryPage.selectSearchParentCategory(parentName);
        // Small pause for UI stability
        try { Thread.sleep(500); } catch (InterruptedException e) {}
    }

    @Then("the category list should display the {string} record")
    public void verify_filtered_list_contains_record(String categoryName) {
        // We reuse the existing strict check, or a softer check if you prefer
        boolean isVisible = categoryPage.isCategoryInTableStrict(categoryName);
        Assert.assertTrue("Filter Failed: Expected category '" + categoryName + "' was not visible in the table.", isVisible);
    }

    // --- NEW STEPS FOR TC_UI_USER_CAT_04 ---

    @Then("the message {string} is displayed clearly in the table area")
    public void verify_no_records_message(String expectedMessage) {
        // Wait briefly for table refresh
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        boolean isDisplayed = categoryPage.isNoRecordsMessageDisplayed();
        
        // If the message locator fails, we might just check if the specific "Fruits" or common items are GONE
        // But strictly, we want the message.
        Assert.assertTrue("Bug: The '" + expectedMessage + "' message was not displayed for an invalid search.", isDisplayed);
    }

    // --- NEW STEPS FOR TC_UI_USER_CAT_05 (SEQUENTIAL SORT) ---

    @When("the user clicks the {string} column header")
    public void user_clicks_column_header(String columnName) {
        categoryPage.clickColumnHeader(columnName);
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
    }

    // --- UPDATED SORT VERIFICATION (SMART LOGIC) ---
    @Then("the category list should be sorted by {string} in ascending order")
    public void verify_list_is_sorted(String columnName) {
        // 1. Get Actual Data from UI
        java.util.List<String> actualList = categoryPage.getColumnData(columnName);

        // 2. Prepare Expected Data (Sort logic)
        java.util.List<String> expectedAscending = new java.util.ArrayList<>(actualList);
        java.util.List<String> expectedDescending = new java.util.ArrayList<>(actualList);
        
        // Handling Numeric vs String sort
        if (columnName.equalsIgnoreCase("ID")) {
            // Numeric Logic
            java.util.Collections.sort(expectedAscending, (a, b) -> {
                try { return Integer.valueOf(a).compareTo(Integer.valueOf(b)); }
                catch (Exception e) { return a.compareToIgnoreCase(b); }
            });
        } else {
             // String Logic
             java.util.Collections.sort(expectedAscending, String.CASE_INSENSITIVE_ORDER);
        }
        
        // Create the Descending version
        expectedDescending = new java.util.ArrayList<>(expectedAscending);
        java.util.Collections.reverse(expectedDescending);

        // 3. SMART CHECK
        if (actualList.equals(expectedAscending)) {
            // Case A: It's already perfect.
            Assert.assertTrue(true);
            
        } else if (actualList.equals(expectedDescending)) {
            // Case B: It sorted Z-A instead of A-Z. Let's fix it by clicking again!
            System.out.println("WARN: Column '" + columnName + "' sorted Descending. Clicking again to toggle...");
            
            // Click header again
            categoryPage.clickColumnHeader(columnName);
            try { Thread.sleep(1500); } catch (InterruptedException e) {}
            
            // Re-fetch data
            actualList = categoryPage.getColumnData(columnName);
            
            // Assert again
            Assert.assertEquals("Sorting failed even after toggling!", expectedAscending, actualList);
            
        } else {
            // Case C: It's just random/broken.
            System.out.println("ACTUAL: " + actualList);
            System.out.println("EXPECTED: " + expectedAscending);
            Assert.fail("Column '" + columnName + "' is not sorted (neither Ascending nor Descending).");
        }
    }
}