package com.example.ui.stepDefinitions.categories;

import com.example.ui.pages.categories.CategoryPage;
import com.example.ui.pages.categories.AddCategoryPage;
import com.example.ui.pages.login.LoginPage;
import io.cucumber.java.en.*;
import com.example.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.Assert;

public class CategoryStepDefinitions {
    WebDriver driver = new ChromeDriver(); // In real project, use a TestBase/Hooks class
    CategoryPage categoryPage = new CategoryPage(driver);
    AddCategoryPage addCategoryPage = new AddCategoryPage(driver);
    LoginPage loginPage = new LoginPage(driver);

    @Given("the user is on the login page")
    public void user_is_on_login_page() {
        driver.get("http://localhost:8080/ui/login");
    }

    @When("the user logs in with valid credentials")
    public void user_logs_in() {
        String username = ConfigReader.get("admin.username");
        String password = ConfigReader.get("admin.password");
        loginPage.login(username, password);
    }

    @Then("the user is on the Category Management page")
    public void user_navigates_to_category_page() {
        // Click the Categories menu link to navigate to the category page
        categoryPage.clickCategoriesMenu();
        // Wait for page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        // Do nothing, as per requirement
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
        // Verify URL or page title
        Assert.assertTrue("Not redirected to category list", driver.getCurrentUrl().contains("categories"));
    }

    @Then("the new category {string} should appear in the list")
    public void verify_category_in_list(String name) {
        Assert.assertTrue(categoryPage.isCategoryVisible(name));
        // driver.quit() removed — should be in Hooks.java
    }


}