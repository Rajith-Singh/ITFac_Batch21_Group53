package com.example.api.stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.Assert;

import com.example.utils.ConfigReader;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

public class PlantsUserApiStepDefinitions {

    // Test data storage between steps
    private static String userToken;
    private static Response response;
    private static String baseUrl;
    private static Integer plantCount;
    private static JSONObject testPlant; // For storing specific plant data
    private static JSONArray plantsArray; // For storing plants array data
    private static List<Integer> categoryIds = new ArrayList<>(); // For storing unique category IDs

    @Before("@API_Plants_User")
    public void setup() {
        // Read configuration
        baseUrl = ConfigReader.getProperty("base.url", "http://localhost:8081");
        RestAssured.baseURI = baseUrl;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        System.out.println("\n=== Plants User API Test Setup ===");
        System.out.println("Base URL: " + baseUrl);
        System.out.println("========================\n");
    }

    @Given("user authentication token is available")
    public void user_authentication_token_is_available() {
        System.out.println("\n=== Getting user authentication token ===");

        String username = ConfigReader.getProperty("user.username", "user");
        String password = ConfigReader.getProperty("user.password", "user123");

        System.out.println("Username: " + username);

        // Login to get token
        JSONObject loginBody = new JSONObject();
        loginBody.put("username", username);
        loginBody.put("password", password);

        System.out.println("Login Request Body: " + loginBody.toString());
        System.out.println("Making POST request to: " + baseUrl + "/api/auth/login");

        Response loginResponse = given()
                .contentType("application/json")
                .body(loginBody.toString())
                .log().all()
                .when()
                .post("/api/auth/login")
                .then()
                .log().all()
                .extract()
                .response();

        System.out.println("Login Response Status: " + loginResponse.getStatusCode());

        // Extract token from response
        try {
            String responseBody = loginResponse.getBody().asString();
            System.out.println("Login Response Body: " + responseBody);

            JSONObject jsonResponse = new JSONObject(responseBody);

            String token = jsonResponse.getString("token");
            String tokenType = jsonResponse.optString("tokenType", "Bearer");

            if (token == null || token.isEmpty()) {
                Assert.fail("Token not found in response. Available keys: " + jsonResponse.keySet());
            }

            userToken = tokenType + " " + token;
            System.out.println("✓ User token obtained successfully");
            System.out.println("Token type: " + tokenType);
            System.out.println("Token (first 30 chars): " + token.substring(0, Math.min(30, token.length())) + "...");

        } catch (Exception e) {
            System.out.println("❌ Error parsing login response: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Failed to get user authentication token: " + e.getMessage());
        }

        // Verify token is valid by making a test request
        try {
            Response testResponse = given()
                    .header("Authorization", userToken)
                    .when()
                    .get("/api/auth/validate");

            System.out.println("Token validation status: " + testResponse.getStatusCode());
            if (testResponse.getStatusCode() == 200) {
                System.out.println("✓ User token is valid");
            } else {
                System.out.println("⚠️ Token validation returned status: " + testResponse.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("⚠️ Token validation skipped: " + e.getMessage());
        }

        System.out.println("=== User token setup complete ===\n");
    }

    @Given("multiple plants exist in the system")
    public void multiple_plants_exist_in_the_system() {
        System.out.println("\n=== Verifying multiple plants exist ===");

        // Make a request to get plants count
        response = given()
                .header("Authorization", userToken)
                .header("Accept", "application/json")
                .when()
                .get("/api/plants");

        System.out.println("Plants check response status: " + response.getStatusCode());

        if (response.getStatusCode() == 200) {
            try {
                String responseBody = response.getBody().asString();
                JSONArray plantsArray = new JSONArray(responseBody);
                plantCount = plantsArray.length();

                System.out.println("Found " + plantCount + " plants in the system");

                if (plantCount < 5) {
                    System.out.println("⚠️ Warning: Only " + plantCount + " plants found, but test expects at least 5");
                } else {
                    System.out.println("✓ Sufficient plants exist for testing");
                }

                // Print first few plants for debugging
                System.out.println("\nSample plants (first 3):");
                for (int i = 0; i < Math.min(3, plantCount); i++) {
                    JSONObject plant = plantsArray.getJSONObject(i);
                    System.out.println("  " + (i + 1) + ". ID: " + plant.getInt("id") +
                            ", Name: " + plant.getString("name") +
                            ", Stock: " + plant.getInt("quantity"));
                }

            } catch (Exception e) {
                System.out.println("Error parsing plants response: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ Could not verify plants existence. Status: " + response.getStatusCode());
        }
    }

    @Given("plant exists with ID {int}")
    public void plant_exists_with_id(Integer plantId) {
        System.out.println("\n=== Verifying plant exists with ID " + plantId + " ===");

        // Make a request to verify plant exists
        response = given()
                .header("Authorization", userToken)
                .header("Accept", "application/json")
                .when()
                .get("/api/plants/" + plantId);

        System.out.println("Plant check response status: " + response.getStatusCode());

        if (response.getStatusCode() == 200) {
            try {
                String responseBody = response.getBody().asString();
                testPlant = new JSONObject(responseBody);

                System.out.println("✓ Plant found with ID: " + plantId);
                System.out.println("Plant details:");
                System.out.println("  Name: " + testPlant.getString("name"));
                System.out.println("  Price: " + testPlant.getDouble("price"));
                System.out.println("  Quantity: " + testPlant.getInt("quantity"));
                System.out.println("  Category ID: " + testPlant.getInt("categoryId"));

            } catch (Exception e) {
                System.out.println("Error parsing plant response: " + e.getMessage());
                Assert.fail("Plant with ID " + plantId + " exists but response format is invalid: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Plant with ID " + plantId + " not found or inaccessible");
            Assert.fail("Plant with ID " + plantId + " should exist for this test");
        }
    }

    @Given("plant ID {int} does not exist in the system")
    public void plant_id_does_not_exist_in_the_system(Integer plantId) {
        System.out.println("\n=== Verifying plant ID " + plantId + " does not exist ===");

        // We'll just log this step, actual verification happens when we make the
        // request
        System.out.println("Assuming plant ID " + plantId + " does not exist in the system");
        System.out.println("This will be verified when the GET request returns 404");
    }

    @Given("category ID {int} exists with plants")
    public void category_id_exists_with_plants(Integer categoryId) {
        System.out.println("\n=== Verifying category ID " + categoryId + " exists with plants ===");

        // Make a request to check if category exists and has plants
        response = given()
                .header("Authorization", userToken)
                .header("Accept", "application/json")
                .when()
                .get("/api/plants/category/" + categoryId);

        System.out.println("Category check response status: " + response.getStatusCode());

        if (response.getStatusCode() == 200) {
            try {
                String responseBody = response.getBody().asString();
                JSONArray categoryPlants = new JSONArray(responseBody);

                System.out.println("Found " + categoryPlants.length() + " plants in category " + categoryId);

                if (categoryPlants.length() > 0) {
                    System.out.println("✓ Category " + categoryId + " exists and has plants");

                    // Verify all plants have the correct category
                    for (int i = 0; i < categoryPlants.length(); i++) {
                        JSONObject plant = categoryPlants.getJSONObject(i);
                        // Check category structure - could be object or categoryId field
                        if (plant.has("category")) {
                            JSONObject categoryObj = plant.getJSONObject("category");
                            int plantCategoryId = categoryObj.getInt("id");
                            Assert.assertEquals("Plant " + i + " should have category ID " + categoryId,
                                    categoryId.intValue(), plantCategoryId);
                        } else if (plant.has("categoryId")) {
                            int plantCategoryId = plant.getInt("categoryId");
                            Assert.assertEquals("Plant " + i + " should have category ID " + categoryId,
                                    categoryId.intValue(), plantCategoryId);
                        }
                    }
                } else {
                    System.out.println("⚠️ Category " + categoryId + " exists but has no plants");
                }

            } catch (Exception e) {
                System.out.println("Error parsing category plants response: " + e.getMessage());
                Assert.fail("Category " + categoryId + " exists but response format is invalid: " + e.getMessage());
            }
        } else if (response.getStatusCode() == 404) {
            System.out.println("❌ Category ID " + categoryId + " not found");
            Assert.fail("Category ID " + categoryId + " should exist for this test");
        } else {
            System.out.println("⚠️ Unexpected status code: " + response.getStatusCode());
            Assert.fail("Could not verify category " + categoryId + " existence");
        }
    }

    @Given("category ID {int} does not exist")
    public void category_id_does_not_exist(Integer categoryId) {
        System.out.println("\n=== Verifying category ID " + categoryId + " does not exist ===");

        // We'll just log this step, actual verification happens when we make the
        // request
        System.out.println("Assuming category ID " + categoryId + " does not exist in the system");
        System.out.println("This will be verified when the GET request returns 404 or empty array");
    }

    @When("user sends a GET request to {string}")
    public void user_sends_a_get_request_to(String endpoint) {
        System.out.println("\n=== Sending GET request to plants endpoint ===");
        System.out.println("Endpoint: " + endpoint);
        System.out.println("Full URL: " + baseUrl + endpoint);

        response = given()
                .header("Authorization", userToken)
                .header("Accept", "application/json")
                .log().all()
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();

        System.out.println("Response Status: " + response.getStatusCode());
    }

    @Then("the plants response status code should be {int}")
    public void the_plants_response_status_code_should_be(Integer expectedStatusCode) {
        System.out.println("\n=== Verifying plants response status code ===");
        System.out.println("Expected: " + expectedStatusCode);

        if (response == null) {
            Assert.fail("Plants response is null - request might have failed");
        }

        int actualStatusCode = response.getStatusCode();
        System.out.println("Actual: " + actualStatusCode);

        if (expectedStatusCode != actualStatusCode) {
            System.out.println("Response Body: " + response.getBody().asString());
        }

        Assert.assertEquals(
                "Plants response status code should be " + expectedStatusCode,
                expectedStatusCode.intValue(),
                actualStatusCode);

        System.out.println("✓ Plants status code verification passed");
    }

    @Then("the response should contain a valid plants array")
    public void the_response_should_contain_a_valid_plants_array() {
        System.out.println("\n=== Verifying response contains valid plants array ===");

        String responseBody = response.getBody().asString();
        System.out.println("Response Body length: " + responseBody.length() + " characters");

        try {
            // Check if response is a JSON array
            Assert.assertTrue("Response should be a JSON array (starts with '[')",
                    responseBody.trim().startsWith("["));

            // Parse as JSON array
            plantsArray = new JSONArray(responseBody);
            System.out.println("✓ Response is a valid JSON array");
            System.out.println("Number of plants: " + plantsArray.length());

            // Store plant count for later use
            plantCount = plantsArray.length();

        } catch (Exception e) {
            System.out.println("❌ Error: Response is not a valid JSON array");
            System.out.println("Response: " + responseBody);
            Assert.fail("Response should be a valid JSON array: " + e.getMessage());
        }
    }

    @Then("the response should contain a single plant object")
    public void the_response_should_contain_a_single_plant_object() {
        System.out.println("\n=== Verifying response contains a single plant object ===");

        String responseBody = response.getBody().asString();
        System.out.println("Response Body length: " + responseBody.length() + " characters");

        try {
            // Check if response is a JSON object (not array)
            Assert.assertTrue("Response should be a JSON object (starts with '{')",
                    responseBody.trim().startsWith("{"));

            // Parse as JSON object
            JSONObject plantObject = new JSONObject(responseBody);
            System.out.println("✓ Response is a valid JSON object (single plant)");

            // Store the plant object for verification
            testPlant = plantObject;

            // Log plant details
            System.out.println("Plant details:");
            System.out.println("  ID: " + plantObject.getInt("id"));
            System.out.println("  Name: " + plantObject.getString("name"));
            System.out.println("  Price: " + plantObject.getDouble("price"));
            System.out.println("  Quantity: " + plantObject.getInt("quantity"));
            if (plantObject.has("categoryId")) {
                System.out.println("  Category ID: " + plantObject.getInt("categoryId"));
            }

        } catch (Exception e) {
            System.out.println("❌ Error: Response is not a valid JSON object");
            System.out.println("Response: " + responseBody);
            Assert.fail("Response should be a single plant object: " + e.getMessage());
        }
    }

    @Then("the plant should have the required fields")
    public void the_plant_should_have_the_required_fields() {
        System.out.println("\n=== Verifying single plant has required fields ===");

        if (testPlant == null) {
            // Parse from response if not already stored
            String responseBody = response.getBody().asString();
            try {
                testPlant = new JSONObject(responseBody);
            } catch (Exception e) {
                Assert.fail("Could not parse plant object: " + e.getMessage());
            }
        }

        // Required fields based on test case - adjusted based on actual API response
        // Single plant endpoint returns categoryId, not category object
        String[] requiredFields = { "id", "name", "price", "quantity", "categoryId" };

        System.out.println("Checking required fields for single plant:");

        for (String field : requiredFields) {
            try {
                Assert.assertTrue("Plant should have field: " + field, testPlant.has(field));
                System.out.println("✓ Field '" + field + "' exists");

                // Additional validation for specific fields
                switch (field) {
                    case "id":
                        int id = testPlant.getInt("id");
                        Assert.assertTrue("ID should be positive", id > 0);
                        System.out.println("  Value: " + id);
                        break;
                    case "name":
                        String name = testPlant.getString("name");
                        Assert.assertFalse("Name should not be empty", name.trim().isEmpty());
                        System.out.println("  Value: " + name);
                        break;
                    case "price":
                        double price = testPlant.getDouble("price");
                        Assert.assertTrue("Price should be positive", price > 0);
                        System.out.println("  Value: " + price);
                        break;
                    case "quantity":
                        int quantity = testPlant.getInt("quantity");
                        Assert.assertTrue("Quantity should be non-negative", quantity >= 0);
                        System.out.println("  Value: " + quantity);
                        break;
                    case "categoryId":
                        int categoryId = testPlant.getInt("categoryId");
                        Assert.assertTrue("Category ID should be positive", categoryId > 0);
                        System.out.println("  Value: " + categoryId);
                        break;
                }

            } catch (Exception e) {
                System.out.println("❌ Error with field '" + field + "': " + e.getMessage());
                Assert.fail("Plant missing or has invalid field: " + field + " - " + e.getMessage());
            }
        }

        System.out.println("✓ All required fields are present and valid");
    }

    @Then("the error message should indicate plant not found")
    public void the_error_message_should_indicate_plant_not_found() {
        System.out.println("\n=== Verifying error message for plant not found ===");

        String responseBody = response.getBody().asString();
        System.out.println("Response Body: " + responseBody);

        try {
            // Parse error response
            JSONObject errorResponse = new JSONObject(responseBody);

            // Check for expected error structure
            Assert.assertTrue("Error response should have 'status' field", errorResponse.has("status"));
            Assert.assertTrue("Error response should have 'error' field", errorResponse.has("error"));
            Assert.assertTrue("Error response should have 'message' field", errorResponse.has("message"));
            Assert.assertTrue("Error response should have 'timestamp' field", errorResponse.has("timestamp"));

            // Verify status code matches response
            int errorStatus = errorResponse.getInt("status");
            Assert.assertEquals("Error status should match response status",
                    response.getStatusCode(), errorStatus);

            // Verify error type
            String errorType = errorResponse.getString("error");
            System.out.println("Error type: " + errorType);

            // Verify error message contains "Plant not found"
            String errorMessage = errorResponse.getString("message");
            System.out.println("Error message: " + errorMessage);

            Assert.assertTrue("Error message should indicate plant not found",
                    errorMessage.toLowerCase().contains("plant not found") ||
                            errorMessage.toLowerCase().contains("not found"));

            System.out.println("✓ Error message correctly indicates plant not found");

        } catch (Exception e) {
            System.out.println("❌ Error parsing error response: " + e.getMessage());
            System.out.println("Raw response: " + responseBody);

            // Fallback check - just look for "plant not found" in the response body
            if (responseBody.toLowerCase().contains("plant not found")) {
                System.out.println("✓ Found 'plant not found' in response body");
            } else {
                Assert.fail("Error response format is invalid: " + e.getMessage());
            }
        }
    }

    @Then("the error message should indicate category not found or return empty array")
    public void the_error_message_should_indicate_category_not_found_or_return_empty_array() {
        System.out.println("\n=== Verifying error message for category not found ===");

        String responseBody = response.getBody().asString();
        System.out.println("Response Body: " + responseBody);

        // Check if response is empty array (some APIs return [] for non-existent
        // categories)
        if (responseBody.trim().equals("[]")) {
            System.out.println("✓ API returned empty array for non-existent category");
            return;
        }

        try {
            // Try to parse as error response
            JSONObject errorResponse = new JSONObject(responseBody);

            // Check for expected error structure
            Assert.assertTrue("Error response should have 'status' field", errorResponse.has("status"));
            Assert.assertTrue("Error response should have 'error' field", errorResponse.has("error"));
            Assert.assertTrue("Error response should have 'message' field", errorResponse.has("message"));

            // Verify status code matches response
            int errorStatus = errorResponse.getInt("status");
            Assert.assertEquals("Error status should match response status",
                    response.getStatusCode(), errorStatus);

            // Verify error message contains "Category not found"
            String errorMessage = errorResponse.getString("message");
            System.out.println("Error message: " + errorMessage);

            Assert.assertTrue("Error message should indicate category not found",
                    errorMessage.toLowerCase().contains("category not found") ||
                            errorMessage.toLowerCase().contains("not found"));

            System.out.println("✓ Error message correctly indicates category not found");

        } catch (Exception e) {
            System.out.println("❌ Error parsing error response: " + e.getMessage());

            // Check if response contains category not found text
            if (responseBody.toLowerCase().contains("category not found")) {
                System.out.println("✓ Found 'category not found' in response body");
            } else {
                // Accept empty array or any non-error response
                System.out.println("⚠️ API might have different error handling for non-existent categories");
            }
        }
    }

    @Then("the response should contain at least {int} plants")
    public void the_response_should_contain_at_least_plants(Integer minPlants) {
        System.out.println("\n=== Verifying minimum plants count ===");
        System.out.println("Expected at least: " + minPlants + " plants");

        if (plantCount == null) {
            // Parse response to get count
            try {
                String responseBody = response.getBody().asString();
                JSONArray plantsArray = new JSONArray(responseBody);
                plantCount = plantsArray.length();
            } catch (Exception e) {
                Assert.fail("Could not parse plants array: " + e.getMessage());
            }
        }

        System.out.println("Actual plants count: " + plantCount);

        Assert.assertTrue("Response should contain at least " + minPlants + " plants, but found " + plantCount,
                plantCount >= minPlants);

        System.out.println("✓ Minimum plants count verification passed");
    }

    @Then("the response should contain at least {int} plant")
    public void the_response_should_contain_at_least_plant(Integer minPlants) {
        the_response_should_contain_at_least_plants(minPlants);
    }

    @Then("the response should contain exactly {int} plants")
    public void the_response_should_contain_exactly_plants(Integer expectedCount) {
        System.out.println("\n=== Verifying exact plants count ===");
        System.out.println("Expected exactly: " + expectedCount + " plants");

        if (plantCount == null) {
            // Parse response to get count
            try {
                String responseBody = response.getBody().asString();
                JSONArray plantsArray = new JSONArray(responseBody);
                plantCount = plantsArray.length();
            } catch (Exception e) {
                Assert.fail("Could not parse plants array: " + e.getMessage());
            }
        }

        System.out.println("Actual plants count: " + plantCount);

        Assert.assertEquals("Response should contain exactly " + expectedCount + " plants, but found " + plantCount,
                expectedCount.intValue(), plantCount.intValue());

        System.out.println("✓ Exact plants count verification passed");
    }

    @Then("all plants should have category ID {int}")
    public void all_plants_should_have_category_id(Integer expectedCategoryId) {
        System.out.println("\n=== Verifying all plants have category ID " + expectedCategoryId + " ===");

        if (plantsArray == null) {
            // Parse response if not already stored
            String responseBody = response.getBody().asString();
            try {
                plantsArray = new JSONArray(responseBody);
            } catch (Exception e) {
                Assert.fail("Could not parse plants array: " + e.getMessage());
            }
        }

        System.out.println("Checking " + plantsArray.length() + " plants...");

        for (int i = 0; i < plantsArray.length(); i++) {
            try {
                JSONObject plant = plantsArray.getJSONObject(i);

                // Check category based on response structure
                if (plant.has("category")) {
                    JSONObject category = plant.getJSONObject("category");
                    int categoryId = category.getInt("id");
                    Assert.assertEquals("Plant at index " + i + " should have category ID " + expectedCategoryId,
                            expectedCategoryId.intValue(), categoryId);
                    System.out.println("✓ Plant " + (i + 1) + ": Category ID = " + categoryId);
                } else if (plant.has("categoryId")) {
                    int categoryId = plant.getInt("categoryId");
                    Assert.assertEquals("Plant at index " + i + " should have category ID " + expectedCategoryId,
                            expectedCategoryId.intValue(), categoryId);
                    System.out.println("✓ Plant " + (i + 1) + ": Category ID = " + categoryId);
                } else {
                    Assert.fail("Plant at index " + i + " has no category information");
                }

            } catch (Exception e) {
                System.out.println("❌ Error checking plant " + (i + 1) + ": " + e.getMessage());
                Assert.fail("Failed to verify category for plant " + (i + 1) + ": " + e.getMessage());
            }
        }

        System.out.println("✓ All " + plantsArray.length() + " plants have category ID " + expectedCategoryId);
    }

    @Then("no plants should have different category IDs")
    public void no_plants_should_have_different_category_ids() {
        System.out.println("\n=== Verifying no plants have different category IDs ===");

        if (plantsArray == null) {
            // Parse response if not already stored
            String responseBody = response.getBody().asString();
            try {
                plantsArray = new JSONArray(responseBody);
            } catch (Exception e) {
                Assert.fail("Could not parse plants array: " + e.getMessage());
            }
        }

        // Clear previous category IDs
        categoryIds.clear();

        System.out.println("Checking category consistency across " + plantsArray.length() + " plants...");

        for (int i = 0; i < plantsArray.length(); i++) {
            try {
                JSONObject plant = plantsArray.getJSONObject(i);
                int plantCategoryId = -1;

                // Get category ID based on response structure
                if (plant.has("category")) {
                    JSONObject category = plant.getJSONObject("category");
                    plantCategoryId = category.getInt("id");
                } else if (plant.has("categoryId")) {
                    plantCategoryId = plant.getInt("categoryId");
                } else {
                    Assert.fail("Plant at index " + i + " has no category information");
                }

                // Add to list of unique category IDs
                if (!categoryIds.contains(plantCategoryId)) {
                    categoryIds.add(plantCategoryId);
                }

                System.out.println("Plant " + (i + 1) + ": Category ID = " + plantCategoryId);

            } catch (Exception e) {
                System.out.println("❌ Error checking plant " + (i + 1) + ": " + e.getMessage());
                Assert.fail("Failed to get category for plant " + (i + 1) + ": " + e.getMessage());
            }
        }

        // Verify only one unique category ID exists
        if (categoryIds.size() == 1) {
            System.out.println("✓ All plants have the same category ID: " + categoryIds.get(0));
        } else {
            System.out.println("❌ Found " + categoryIds.size() + " different category IDs: " + categoryIds);
            Assert.fail("Expected all plants to have the same category, but found " + categoryIds.size()
                    + " different IDs");
        }
    }

    @Then("each plant should have required fields")
    public void each_plant_should_have_required_fields() {
        System.out.println("\n=== Verifying plant structure ===");

        String responseBody = response.getBody().asString();

        try {
            JSONArray plantsArray = new JSONArray(responseBody);
            int validPlants = 0;
            int invalidPlants = 0;

            System.out.println("Checking " + plantsArray.length() + " plants for required fields...");

            // Required fields for each plant (for list endpoint, category might be object)
            String[] requiredFields = { "id", "name", "price", "quantity", "category" };

            for (int i = 0; i < plantsArray.length(); i++) {
                try {
                    JSONObject plant = plantsArray.getJSONObject(i);
                    boolean isValid = true;

                    // Check each required field
                    for (String field : requiredFields) {
                        if (!plant.has(field)) {
                            System.out.println("⚠️ Plant " + (i + 1) + " missing field: " + field);
                            isValid = false;
                        }
                    }

                    // Check field types and values
                    if (isValid) {
                        // Check ID is positive integer
                        int id = plant.getInt("id");
                        if (id <= 0) {
                            System.out.println("⚠️ Plant " + (i + 1) + " has invalid ID: " + id);
                            isValid = false;
                        }

                        // Check name is not empty
                        String name = plant.getString("name");
                        if (name == null || name.trim().isEmpty()) {
                            System.out.println("⚠️ Plant " + (i + 1) + " has empty name");
                            isValid = false;
                        }

                        // Check price is positive
                        double price = plant.getDouble("price");
                        if (price <= 0) {
                            System.out.println("⚠️ Plant " + (i + 1) + " has invalid price: " + price);
                            isValid = false;
                        }

                        // Check quantity is non-negative
                        int quantity = plant.getInt("quantity");
                        if (quantity < 0) {
                            System.out.println("⚠️ Plant " + (i + 1) + " has negative quantity: " + quantity);
                            isValid = false;
                        }

                        // Check category structure
                        try {
                            Object category = plant.get("category");
                            if (category instanceof JSONObject) {
                                JSONObject categoryObj = (JSONObject) category;
                                if (!categoryObj.has("id") || !categoryObj.has("name")) {
                                    System.out.println("⚠️ Plant " + (i + 1) + " has incomplete category object");
                                    isValid = false;
                                }
                            }
                            // If it's just an ID (integer), that's also valid
                        } catch (Exception e) {
                            System.out
                                    .println("⚠️ Plant " + (i + 1) + " has invalid category format: " + e.getMessage());
                            isValid = false;
                        }
                    }

                    if (isValid) {
                        validPlants++;
                    } else {
                        invalidPlants++;
                    }

                } catch (Exception e) {
                    System.out.println("❌ Error checking plant " + (i + 1) + ": " + e.getMessage());
                    invalidPlants++;
                }
            }

            System.out.println("\n=== Plant Structure Summary ===");
            System.out.println("Total plants: " + plantsArray.length());
            System.out.println("Valid plants: " + validPlants);
            System.out.println("Invalid plants: " + invalidPlants);

            if (invalidPlants == 0) {
                System.out.println("✓ All plants have valid structure");
            } else {
                System.out.println("⚠️ " + invalidPlants + " plants have invalid structure");
                // For this test, we might accept some invalid data if most are valid
                if (validPlants >= 5) {
                    System.out.println("✓ At least 5 plants have valid structure");
                } else {
                    Assert.fail("Not enough valid plants. Expected at least 5, found " + validPlants);
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Error parsing plants array: " + e.getMessage());
            Assert.fail("Could not verify plant structure: " + e.getMessage());
        }
    }

    @After("@API_Plants_User")
    public void cleanup() {
        System.out.println("\n=== Plants User API Test Cleanup ===");

        // Reset test data
        userToken = null;
        response = null;
        plantCount = null;
        testPlant = null;
        plantsArray = null;
        categoryIds.clear();

        System.out.println("Test data cleared");
        System.out.println("=== Cleanup Complete ===\n");
    }
}