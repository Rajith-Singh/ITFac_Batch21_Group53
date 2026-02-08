package com.example.ui.stepDefinitions.login;

import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;
import com.example.utils.DriverManagerP;
import com.example.ui.pages.login.LoginPageP;
import com.example.ui.pages.plants.PlantListPageP;

public class LoginSteps {
    private WebDriver driver;
    private LoginPageP loginPage;
    private PlantListPageP plantListPage;
    private String baseUrl = "http://localhost:8081";

    public LoginSteps() {
        // Constructor should not access driver as it might not be initialized yet
    }

    @Given("user is logged in as admin")
    public void userIsLoggedInAsAdmin() {
        // Initialize driver if not already done
        driver = DriverManagerP.getDriver();
        if (driver == null) {
            DriverManagerP.initializeDriver();
            driver = DriverManagerP.getDriver();
        }

        loginPage = new LoginPageP(driver);
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
        plantListPage = new PlantListPageP(driver);
        plantListPage.navigateToPlantsList(baseUrl);
        assert plantListPage.isPlantListPageDisplayed() : "Plants list page is not displayed";
    }

    @Given("I am logged in as {string}")
    public void iAmLoggedInAs(String userType) {
        driver = DriverManagerP.getDriver();
        if (driver == null) {
            DriverManagerP.initializeDriver();
            driver = DriverManagerP.getDriver();
        }

        loginPage = new LoginPageP  (driver);
        String username = userType.equalsIgnoreCase("admin") ? "admin" : "Testuser";
        String password = userType.equalsIgnoreCase("admin") ? "admin123" : "test123";
        loginPage.loginAsAdmin(baseUrl, username, password);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
