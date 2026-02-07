package com.example.api.stepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import com.example.utils.ConfigReader; 

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CategoryApiStepDefinitions {

    private static final String BASE_URL = com.example.utils.ConfigReader.get("base.url");
    private String authToken;
    private Response response;

    // --- EXISTING CODE STARTS HERE ---

    @Given("the API server is up and running")
    public void api_server_is_up() {
        RestAssured.baseURI = BASE_URL;
    }

    @Given("the user is authenticated as {string}")
    public void user_is_authenticated(String role) {
        // 1. Get credentials from ConfigReader
        String username = ConfigReader.get(role + ".username");
        String password = ConfigReader.get(role + ".password");

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
            authToken = loginResponse.jsonPath().getString("token"); 
        } else {
            Assert.fail("Failed to authenticate via API. Status: " + loginResponse.getStatusCode());
        }
    }

    @When("the user sends a GET request to {string}")
    public void user_sends_get_request(String endpoint) {
        // Send GET request with the Bearer Token
        response = given()
                .header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json")
                .when()
                .get(endpoint);
    }

    @Then("the API response status code should be {int}")
    public void verify_status_code(int expectedStatusCode) {
        System.out.println("Response Body: " + response.getBody().asString()); // Log for debugging
        response.then().statusCode(expectedStatusCode);
    }

    @Then("the response body should contain the field {string} with value {int}")
    public void verify_body_field_int_value(String key, int value) {
        response.then().body(key, equalTo(value));
    }

    @Then("the response body should contain the field {string}")
    public void verify_body_has_field(String key) {
        response.then().body("$", hasKey(key)); 
    }

    @Then("the response body should contain the field {string} with string value {string}")
    public void verify_body_field_string_value(String key, String expectedValue) {
        // This validates fields like "error": "NOT_FOUND" or "message": "Category not found..."
        response.then().body(key, equalTo(expectedValue));
    }

    @Given("the user provides an invalid authentication token")
    public void user_provides_invalid_token() {
        // Set a garbage token to simulate an unauthenticated/expired session
        this.authToken = "invalid_token_12345";
    }

    @When("the user sends a POST request to {string} with name {string} and no parent")
    public void user_sends_post_category_main(String endpoint, String categoryName) {
        // 1. Construct the JSON Payload
        // We do NOT send 'id' (it's auto-generated).
        // We set 'parent' to null because it's a Main Category.
        String payload = "{\n" +
                "  \"name\": \"" + categoryName + "\",\n" +
                "  \"parent\": null,\n" +
                "  \"subCategories\": []\n" +
                "}";

        // 2. Send the POST request
        response = given()
                .header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post(endpoint);
    }

    // --- NEW CODE INTEGRATED BELOW (For Sub-Category Creation) ---

    @When("the user sends a POST request to {string} with name {string} and parent ID {int}")
    public void user_sends_post_category_sub(String endpoint, String subCategoryName, int parentId) {
        // 1. Construct the JSON Payload with the Parent Object
        // Format: { "name": "...", "parent": { "id": 123 }, "subCategories": [] }
        String payload = "{\n" +
                "  \"name\": \"" + subCategoryName + "\",\n" +
                "  \"parent\": { \"id\": " + parentId + " },\n" +
                "  \"subCategories\": []\n" +
                "}";

        // 2. Send the POST request
        response = given()
                .header("Authorization", "Bearer " + authToken)
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post(endpoint);
    }
}