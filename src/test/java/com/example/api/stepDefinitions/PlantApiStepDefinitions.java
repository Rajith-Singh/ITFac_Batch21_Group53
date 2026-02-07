package com.example.api.stepDefinitions;

import io.cucumber.java.en.When;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class PlantApiStepDefinitions {

    // We reuse the 'token' and 'response' variables from the shared context 
    // or access them via a shared World object. 
    // For simplicity, assuming your existing CategoryApiStepDefinitions stores them statically 
    // or you can extend a BaseStep definition.
    // If you are using the static variables from your previous file:
    
    @When("the user sends a DELETE request to {string}")
    public void user_sends_delete_request(String endpoint) {
        String url = CategoryApiStepDefinitions.baseUrl + endpoint;
        String token = CategoryApiStepDefinitions.userToken; // Accessing the token logged in by the "Given" step

        System.out.println("Sending DELETE Request to: " + url);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .delete(url);

        // Store response back in the shared variable so the "Then" steps can verify it
        CategoryApiStepDefinitions.lastResponse = response;
        
        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @When("the user sends a GET request to {string} with page {int} and size {int}")
    public void user_sends_get_request_with_pagination(String endpoint, int page, int size) {
        String url = CategoryApiStepDefinitions.baseUrl + endpoint;
        String token = CategoryApiStepDefinitions.userToken;

        System.out.println("Sending GET Request to: " + url + " [page=" + page + ", size=" + size + "]");

        // Send GET request with Query Parameters
        CategoryApiStepDefinitions.lastResponse = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(url);

        System.out.println("Response Status: " + CategoryApiStepDefinitions.lastResponse.getStatusCode());
    }

    @When("the user sends a GET request to {string} with sort {string}")
    public void user_sends_get_request_with_sort(String endpoint, String sortParam) {
        String url = CategoryApiStepDefinitions.baseUrl + endpoint;
        String token = CategoryApiStepDefinitions.userToken;

        System.out.println("Sending GET Request to: " + url + " [sort=" + sortParam + "]");

        // Send GET request with 'sort' Query Parameter
        CategoryApiStepDefinitions.lastResponse = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .queryParam("sort", sortParam)
                .when()
                .get(url);

        System.out.println("Response Status: " + CategoryApiStepDefinitions.lastResponse.getStatusCode());
    }

    // --- NEW CODE INTEGRATED BELOW ---

    @When("the user sends a GET request to {string} without authentication")
    public void user_sends_get_request_no_auth(String endpoint) {
        String url = CategoryApiStepDefinitions.baseUrl + endpoint;

        System.out.println("Sending GET Request (No Auth) to: " + url);

        // Send GET request WITHOUT adding the Authorization header
        CategoryApiStepDefinitions.lastResponse = given()
                .contentType("application/json")
                .when()
                .get(url);

        System.out.println("Response Status: " + CategoryApiStepDefinitions.lastResponse.getStatusCode());
    }
}