package com.example.ui.stepDefinitions.login;

import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;
import com.example.utils.DriverManager;
import com.example.ui.pages.login.LoginPage;
import com.example.ui.pages.plants.PlantListPage;

public class LoginSteps {
    private WebDriver driver;
    private LoginPage loginPage;
    private PlantListPage plantListPage;
    private String baseUrl = "http://localhost:8080";

    public LoginSteps() {
        this.driver = DriverManager.getDriver();
        this.loginPage = new LoginPage(driver);
        this.plantListPage = new PlantListPage(driver);
    }

    @Given("user is logged in as admin")
    public void userIsLoggedInAsAdmin() {
        // Initialize driver if not already done
        if (driver == null) {
            DriverManager.initializeDriver();
            driver = DriverManager.getDriver();
        }
        
        loginPage = new LoginPage(driver);
        // Assuming admin credentials - adjust based on your application
        loginPage.loginAsAdmin(baseUrl, "admin", "admin123");
        
        // Wait for login to complete
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Given("user is on the Plants List page")
    public void userIsOnThePlantsListPage() {
        plantListPage = new PlantListPage(driver);
        plantListPage.navigateToPlantsList(baseUrl);
        assert plantListPage.isPlantListPageDisplayed() : "Plants list page is not displayed";
    }

    @Given("I am logged in as {string}")
    public void iAmLoggedInAs(String userType) {
        if (driver == null) {
            DriverManager.initializeDriver();
            driver = DriverManager.getDriver();
        }
        
        loginPage = new LoginPage(driver);
        String username = userType.equalsIgnoreCase("admin") ? "admin" : "user";
        String password = userType.equalsIgnoreCase("admin") ? "admin123" : "user123";
        loginPage.loginAsAdmin(baseUrl, username, password);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
