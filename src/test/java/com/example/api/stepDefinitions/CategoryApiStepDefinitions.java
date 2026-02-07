package com.example.api.stepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.example.utils.ConfigReader; 

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CategoryApiStepDefinitions {

    // --- UPDATED VARIABLES (Public Static for Shared Access) ---
    public static String baseUrl = ConfigReader.getProperty("base.url");
    public static String userToken;      // Renamed from 'authToken' to match PlantApi file
    public static Response lastResponse; // Renamed from 'response' to match PlantApi file

    // --- EXISTING CODE (With variable names updated) ---

    @Given("the API server is up and running")
    public void api_server_is_up() {
        RestAssured.baseURI = baseUrl;
    }

    @Given("the user is authenticated as {string}")
    public void user_is_authenticated(String role) {
        // 1. Get credentials from ConfigReader
        String username = ConfigReader.getProperty(role + ".username");
        String password = ConfigReader.getProperty(role + ".password");

        // 2. Prepare Login Payload
        String loginPayload = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";

        // 3. Send POST request to login endpoint
        Response loginResponse = given()
                .header("Content-Type", "application/json")
                .body(loginPayload)
                .when()
                .post("/api/auth/login");

        // 4. Extract Token
        if (loginResponse.getStatusCode() == 200) {
            userToken = loginResponse.jsonPath().getString("token"); 
        } else {
            // Using Assert.fail requires org.junit.Assert import, or throw RuntimeException
            throw new RuntimeException("Failed to authenticate via API. Status: " + loginResponse.getStatusCode());
        }
    }

    @When("the user sends a GET request to {string}")
    public void user_sends_get_request(String endpoint) {
        // Send GET request with the Bearer Token
        lastResponse = given()
                .header("Authorization", "Bearer " + userToken)
                .header("Content-Type", "application/json")
                .when()
                .get(endpoint);
    }

    @Then("the API response status code should be {int}")
    public void verify_status_code(int expectedStatusCode) {
        // Log for debugging (handling null response safely)
        if(lastResponse != null) {
            System.out.println("Response Body: " + lastResponse.getBody().asString()); 
            lastResponse.then().statusCode(expectedStatusCode);
        }
    }

    @Then("the response body should contain the field {string} with value {int}")
    public void verify_body_field_int_value(String key, int value) {
        lastResponse.then().body(key, equalTo(value));
    }

    @Then("the response body should contain the field {string}")
    public void verify_body_has_field(String key) {
        lastResponse.then().body("$", hasKey(key)); 
    }

    @Then("the response body should contain the field {string} with string value {string}")
    public void verify_body_field_string_value(String key, String expectedValue) {
        lastResponse.then().body(key, equalTo(expectedValue));
    }

    @Given("the user provides an invalid authentication token")
    public void user_provides_invalid_token() {
        // Set a garbage token to simulate an unauthenticated/expired session
        userToken = "invalid_token_12345";
    }

    @When("the user sends a POST request to {string} with name {string} and no parent")
    public void user_sends_post_category_main(String endpoint, String categoryName) {
        String payload = "{\n" +
                "  \"name\": \"" + categoryName + "\",\n" +
                "  \"parent\": null,\n" +
                "  \"subCategories\": []\n" +
                "}";

        lastResponse = given()
                .header("Authorization", "Bearer " + userToken)
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post(endpoint);
    }

    @When("the user sends a POST request to {string} with name {string} and parent ID {int}")
    public void user_sends_post_category_sub(String endpoint, String subCategoryName, int parentId) {
        String payload = "{\n" +
                "  \"name\": \"" + subCategoryName + "\",\n" +
                "  \"parent\": { \"id\": " + parentId + " },\n" +
                "  \"subCategories\": []\n" +
                "}";

        lastResponse = given()
                .header("Authorization", "Bearer " + userToken)
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post(endpoint);
    }
}