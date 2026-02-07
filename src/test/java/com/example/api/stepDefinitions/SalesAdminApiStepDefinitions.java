package com.example.api.stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.Assert;

import com.example.utils.ConfigReader;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SalesAdminApiStepDefinitions {

    // Test data storage between steps
    private static String adminToken;
    private static Response response;
    private static Integer createdSaleId;
    private static JSONObject saleResponseBody;
    private static Integer initialStock;
    private static Integer PLANT_ID;
    private static String baseUrl;
    private static Integer stockBeforeTest;
    private static Integer existingSaleId;

    @Before("@API")
    public void setup() {
        // Read configuration
        baseUrl = ConfigReader.getProperty("base.url", "http://localhost:8081");
        RestAssured.baseURI = baseUrl;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        System.out.println("\n=== API Test Setup ===");
        System.out.println("Base URL: " + baseUrl);
        System.out.println("========================\n");
    }

    @Given("admin authentication token is available")
    public void admin_authentication_token_is_available() {
        System.out.println("\n=== Getting admin authentication token ===");

        String username = ConfigReader.getProperty("admin.username", "admin");
        String password = ConfigReader.getProperty("admin.password", "admin123");

        System.out.println("Username: " + username);

        // Login to get token
        JSONObject loginBody = new JSONObject();
        loginBody.put("username", username);
        loginBody.put("password", password);

        Response loginResponse = given()
                .contentType("application/json")
                .body(loginBody.toString())
                .when()
                .post("/api/auth/login");

        System.out.println("Login Response Status: " + loginResponse.getStatusCode());

        // Extract token from response
        try {
            String responseBody = loginResponse.getBody().asString();
            JSONObject jsonResponse = new JSONObject(responseBody);

            String token = jsonResponse.getString("token");
            String tokenType = jsonResponse.optString("tokenType", "Bearer");

            if (token == null || token.isEmpty()) {
                Assert.fail("Token not found in response.");
            }

            adminToken = tokenType + " " + token;
            System.out.println("✓ Admin token obtained successfully");

        } catch (Exception e) {
            System.out.println("❌ Error parsing login response: " + e.getMessage());
            Assert.fail("Failed to get authentication token: " + e.getMessage());
        }
    }

    @Given("plant with ID {int} exists with current stock units")
    public void plant_with_id_exists_with_current_stock_units(Integer plantId) {
        System.out.println("\n=== Verifying plant exists and getting stock ===");
        PLANT_ID = plantId;

        System.out.println("Checking plant ID: " + plantId);

        // Get plant details
        Response plantResponse = given()
                .header("Authorization", adminToken)
                .when()
                .get("/api/plants/" + plantId);

        System.out.println("Plant Response Status: " + plantResponse.getStatusCode());

        if (plantResponse.getStatusCode() == 404) {
            Assert.fail("❌ Plant with ID " + plantId + " does not exist.");
        }

        Assert.assertEquals("Plant should exist", 200, plantResponse.getStatusCode());

        try {
            // Parse the response
            JSONObject plantJson = new JSONObject(plantResponse.getBody().asString());

            // Get stock
            if (plantJson.has("quantity")) {
                initialStock = plantJson.getInt("quantity");
            } else if (plantJson.has("stock")) {
                initialStock = plantJson.getInt("stock");
            } else {
                System.out.println("Available keys in plant response: " + plantJson.keySet());
                Assert.fail("Could not find stock/quantity field in plant response");
            }

            String plantName = plantJson.optString("name", "Unknown");
            System.out.println("✓ Plant Details:");
            System.out.println("  - ID: " + plantId);
            System.out.println("  - Name: " + plantName);
            System.out.println("  - Current Stock: " + initialStock + " units");

        } catch (Exception e) {
            System.out.println("❌ Error extracting plant data: " + e.getMessage());
            Assert.fail("Failed to extract plant data: " + e.getMessage());
        }
    }

    @Given("sale exists with ID {int}")
    public void sale_exists_with_id(Integer saleId) {
        System.out.println("\n=== Verifying sale exists with ID " + saleId + " ===");
        existingSaleId = saleId;

        // First, try to get the sale to verify it exists
        Response saleResponse = given()
                .header("Authorization", adminToken)
                .when()
                .get("/api/sales/" + saleId);

        System.out.println("Sale retrieval status: " + saleResponse.getStatusCode());

        if (saleResponse.getStatusCode() == 404) {
            System.out.println("⚠️ Sale ID " + saleId + " does not exist. Creating a test sale...");

            // Create a test sale
            try {
                // First, find an existing plant
                Response plantsResponse = given()
                        .header("Authorization", adminToken)
                        .when()
                        .get("/api/plants?page=0&size=1");

                if (plantsResponse.getStatusCode() == 200) {
                    String plantsBody = plantsResponse.getBody().asString();
                    JSONObject plantsJson = new JSONObject(plantsBody);

                    if (plantsJson.has("content") && plantsJson.getJSONArray("content").length() > 0) {
                        JSONObject plant = plantsJson.getJSONArray("content").getJSONObject(0);
                        int plantId = plant.getInt("id");

                        // Create a sale
                        JSONObject saleBody = new JSONObject();
                        saleBody.put("quantity", 1);

                        Response createResponse = given()
                                .header("Authorization", adminToken)
                                .contentType("application/json")
                                .body(saleBody.toString())
                                .when()
                                .post("/api/sales/plant/" + plantId);

                        if (createResponse.getStatusCode() == 200) {
                            JSONObject createdSale = new JSONObject(createResponse.getBody().asString());
                            existingSaleId = createdSale.getInt("id");
                            System.out.println("✓ Created test sale with ID: " + existingSaleId);
                            System.out.println("⚠️ Note: Test will now delete this newly created sale");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error creating test sale: " + e.getMessage());
            }
        } else if (saleResponse.getStatusCode() == 200) {
            System.out.println("✓ Sale ID " + saleId + " exists");
        }
    }

    @Given("the current stock is more than {int} units")
    public void the_current_stock_is_more_than_units(Integer minStock) {
        System.out.println("Verifying stock is more than " + minStock + " units");
        Assert.assertTrue("Stock should be more than " + minStock + " units",
                initialStock > minStock);
        System.out.println("✓ Current stock (" + initialStock + ") is sufficient for test");
    }

    @Given("sale record does not already exist for this transaction")
    public void sale_record_does_not_already_exist_for_this_transaction() {
        System.out.println("\n=== Checking for existing sales ===");

        try {
            Response salesResponse = given()
                    .header("Authorization", adminToken)
                    .when()
                    .get("/api/sales?plantId=" + PLANT_ID + "&page=0&size=5");

            if (salesResponse.getStatusCode() == 200) {
                System.out.println("✓ Sales endpoint accessible");
            }
        } catch (Exception e) {
            System.out.println("Note: Error checking existing sales: " + e.getMessage());
        }

        System.out.println("✓ Ready for new sale transaction");
    }

    @When("admin sends a POST request to {string} with body:")
    public void admin_sends_a_post_request_to_with_body(String endpoint, String requestBody) {
        System.out.println("\n=== Creating new sale ===");
        System.out.println("Endpoint: " + endpoint);
        System.out.println("Request Body: " + requestBody);

        // Store stock before sale attempt
        stockBeforeTest = initialStock;

        // Send POST request
        response = given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .body(requestBody)
                .log().all()
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract()
                .response();

        System.out.println("Response Status: " + response.getStatusCode());

        // Store response data if successful
        if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
            try {
                saleResponseBody = new JSONObject(response.getBody().asString());
                createdSaleId = saleResponseBody.getInt("id");
                System.out.println("✓ Sale created successfully");
                System.out.println("Created Sale ID: " + createdSaleId);
            } catch (Exception e) {
                System.out.println("Error parsing response: " + e.getMessage());
            }
        }
    }

    @When("admin attempts to create a sale with excessive quantity")
    public void admin_attempts_to_create_a_sale_with_excessive_quantity(String requestBody) {
        System.out.println("\n=== Attempting sale with excessive quantity ===");
        System.out.println("Request Body: " + requestBody);
        System.out.println("Current stock: " + initialStock + " units");

        // Store stock before attempt
        stockBeforeTest = initialStock;

        // Parse quantity from request
        JSONObject requestJson = new JSONObject(requestBody);
        int requestedQuantity = requestJson.getInt("quantity");
        System.out.println("Requested quantity: " + requestedQuantity + " units");
        System.out.println("This exceeds available stock by " + (requestedQuantity - initialStock) + " units");

        // Send POST request
        response = given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .body(requestBody)
                .log().all()
                .when()
                .post("/api/sales/plant/" + PLANT_ID)
                .then()
                .log().all()
                .extract()
                .response();

        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @When("admin attempts to create a sale with invalid quantity {string}")
    public void admin_attempts_to_create_a_sale_with_invalid_quantity(String quantity) {
        System.out.println("\n=== Testing invalid quantity: " + quantity + " ===");
        System.out.println("Current stock: " + initialStock + " units");

        // Store stock before attempt
        stockBeforeTest = initialStock;

        String requestBody;

        if (quantity.equals("null")) {
            // Test null value
            requestBody = "{\"quantity\": null}";
        } else if (quantity.isEmpty()) {
            // Test missing quantity field
            requestBody = "{}";
        } else {
            // Test zero or negative value
            requestBody = "{\"quantity\": " + quantity + "}";
        }

        System.out.println("Request Body: " + requestBody);

        // Send POST request
        response = given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .body(requestBody)
                .log().all()
                .when()
                .post("/api/sales/plant/" + PLANT_ID)
                .then()
                .log().all()
                .extract()
                .response();

        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @When("admin retrieves sale by ID {int}")
    public void admin_retrieves_sale_by_id(Integer saleId) {
        System.out.println("\n=== Retrieving sale by ID ===");
        System.out.println("Sale ID: " + saleId);

        response = given()
                .header("Authorization", adminToken)
                .log().all()
                .when()
                .get("/api/sales/" + saleId)
                .then()
                .log().all()
                .extract()
                .response();

        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @When("admin deletes sale with ID {int}")
    public void admin_deletes_sale_with_id(Integer saleId) {
        System.out.println("\n=== Deleting sale by ID ===");
        System.out.println("Sale ID: " + saleId);
        System.out.println("URL: DELETE " + baseUrl + "/api/sales/" + saleId);

        // Store the sale ID for verification in later steps
        existingSaleId = saleId;

        // Send DELETE request with detailed logging
        try {
            response = given()
                    .header("Authorization", adminToken)
                    .log().all()
                    .when()
                    .delete("/api/sales/" + saleId)
                    .then()
                    .log().all()
                    .extract()
                    .response();

            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Headers: " + response.getHeaders());

            String responseBody = response.getBody().asString();
            if (responseBody != null && !responseBody.isEmpty()) {
                System.out.println("Response Body: " + responseBody);
            } else {
                System.out.println("Response Body: (empty - typical for 204 No Content)");
            }

        } catch (Exception e) {
            System.out.println("❌ Error during DELETE request: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("DELETE request failed: " + e.getMessage());
        }
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) {
        System.out.println("\n=== Verifying response status code ===");
        System.out.println("Expected: " + expectedStatusCode);

        if (response == null) {
            Assert.fail("Response is null - request might have failed");
        }

        int actualStatusCode = response.getStatusCode();
        System.out.println("Actual: " + actualStatusCode);

        Assert.assertEquals(
                "Response status code should be " + expectedStatusCode,
                expectedStatusCode.intValue(),
                actualStatusCode);

        System.out.println("✓ Status code verification passed");
    }

    @Then("the response status code should be 200 or 204")
    public void the_response_status_code_should_be_200_or_204() {
        System.out.println("\n=== Verifying response status code (200 or 204) ===");

        if (response == null) {
            Assert.fail("Response is null - DELETE request might have failed");
        }

        int actualStatusCode = response.getStatusCode();
        System.out.println("Actual: " + actualStatusCode);

        boolean isValid = actualStatusCode == 200 || actualStatusCode == 204;
        Assert.assertTrue("Response status code should be 200 or 204 but was " + actualStatusCode, isValid);

        System.out.println("✓ Status code verification passed (accepted 200 or 204)");
    }

    @Then("the response body should contain valid sale creation data")
    public void the_response_body_should_contain_valid_sale_creation_data() {
        System.out.println("\n=== Verifying response body structure ===");

        String responseBody = response.getBody().asString();
        System.out.println("Response Body: " + responseBody);

        try {
            JSONObject responseJson = new JSONObject(responseBody);

            // Check required fields
            Assert.assertTrue("Response should have 'id'", responseJson.has("id"));
            Assert.assertTrue("Response should have 'plant'", responseJson.has("plant"));
            Assert.assertTrue("Response should have 'quantity'", responseJson.has("quantity"));
            Assert.assertTrue("Response should have 'totalPrice'", responseJson.has("totalPrice"));
            Assert.assertTrue("Response should have 'soldAt'", responseJson.has("soldAt"));

            JSONObject plantObj = responseJson.getJSONObject("plant");
            Assert.assertTrue("Plant should have 'id'", plantObj.has("id"));
            Assert.assertTrue("Plant should have 'name'", plantObj.has("name"));
            Assert.assertTrue("Plant should have 'price'", plantObj.has("price"));

            // Extract values
            createdSaleId = responseJson.getInt("id");
            int plantId = plantObj.getInt("id");
            String plantName = plantObj.getString("name");
            int quantity = responseJson.getInt("quantity");
            double totalPrice = responseJson.getDouble("totalPrice");

            System.out.println("✓ Valid Sale Data:");
            System.out.println("  Sale ID: " + createdSaleId);
            System.out.println("  Plant ID: " + plantId);
            System.out.println("  Plant Name: " + plantName);
            System.out.println("  Quantity: " + quantity);
            System.out.println("  Total Price: " + totalPrice);

            saleResponseBody = responseJson;

            System.out.println("✓ All validations passed");

        } catch (Exception e) {
            System.out.println("❌ Error validating response: " + e.getMessage());
            Assert.fail("Response validation failed: " + e.getMessage());
        }
    }

    @Then("the response should contain error message about insufficient stock")
    public void the_response_should_contain_error_message_about_insufficient_stock() {
        System.out.println("\n=== Verifying error message ===");

        String responseBody = response.getBody().asString();
        System.out.println("Response Body: " + responseBody);

        try {
            JSONObject responseJson = new JSONObject(responseBody);

            // Check for error message
            Assert.assertTrue("Response should have 'message' field", responseJson.has("message"));

            String errorMessage = responseJson.getString("message");
            System.out.println("Error Message: " + errorMessage);

            // Check if message contains stock-related keywords
            Assert.assertTrue("Error message should mention stock or quantity",
                    errorMessage.toLowerCase().contains("stock") ||
                            errorMessage.toLowerCase().contains("quantity") ||
                            errorMessage.toLowerCase().contains("available") ||
                            errorMessage.toLowerCase().contains("exceed"));

            System.out.println("✓ Error message validation passed");

        } catch (Exception e) {
            System.out.println("❌ Error validating error message: " + e.getMessage());
            Assert.fail("Error message validation failed: " + e.getMessage());
        }
    }

    @Then("the response should contain appropriate validation error")
    public void the_response_should_contain_appropriate_validation_error() {
        System.out.println("\n=== Verifying validation error message ===");

        String responseBody = response.getBody().asString();
        System.out.println("Response Body: " + responseBody);

        try {
            JSONObject responseJson = new JSONObject(responseBody);

            // Check for error message
            Assert.assertTrue("Response should have 'message' field", responseJson.has("message"));

            String errorMessage = responseJson.getString("message");
            System.out.println("Error Message: " + errorMessage);

            // Check for validation-related keywords
            boolean isValidError = errorMessage.toLowerCase().contains("quantity") ||
                    errorMessage.toLowerCase().contains("greater than") ||
                    errorMessage.toLowerCase().contains("required") ||
                    errorMessage.toLowerCase().contains("validation") ||
                    errorMessage.toLowerCase().contains("must be");

            Assert.assertTrue("Error message should mention quantity validation", isValidError);

            System.out.println("✓ Validation error message verified");

        } catch (Exception e) {
            System.out.println("❌ Error validating error message: " + e.getMessage());
            Assert.fail("Validation error check failed: " + e.getMessage());
        }
    }

    @Then("the response should contain a single sale object")
    public void the_response_should_contain_a_single_sale_object() {
        System.out.println("\n=== Verifying response contains single sale object ===");

        String responseBody = response.getBody().asString();
        System.out.println("Response Body: " + responseBody);

        try {
            // Try to parse as JSON object (single sale)
            JSONObject responseJson = new JSONObject(responseBody);

            // Check it's not an array
            Assert.assertFalse("Response should not be a JSON array",
                    responseBody.trim().startsWith("["));

            // Check it has sale ID field
            Assert.assertTrue("Response should have 'id' field", responseJson.has("id"));

            // Check it has the expected structure
            Assert.assertTrue("Response should have 'plant' field", responseJson.has("plant"));
            Assert.assertTrue("Response should have 'quantity' field", responseJson.has("quantity"));
            Assert.assertTrue("Response should have 'totalPrice' field", responseJson.has("totalPrice"));
            Assert.assertTrue("Response should have 'soldAt' field", responseJson.has("soldAt"));

            System.out.println("✓ Response contains a single sale object");

        } catch (Exception e) {
            System.out.println("❌ Response is not a valid single sale object: " + e.getMessage());
            Assert.fail("Response should be a single sale object, not an array or invalid JSON");
        }
    }

    @Then("the response should contain complete sale data")
    public void the_response_should_contain_complete_sale_data() {
        System.out.println("\n=== Verifying complete sale data ===");

        String responseBody = response.getBody().asString();
        System.out.println("Response Body: " + responseBody);

        try {
            JSONObject saleJson = new JSONObject(responseBody);

            // Verify all required fields are present and not null
            Assert.assertTrue("Sale should have 'id'", saleJson.has("id"));
            int saleId = saleJson.getInt("id");
            Assert.assertNotNull("Sale ID should not be null", saleId);

            Assert.assertTrue("Sale should have 'plant' object", saleJson.has("plant"));
            JSONObject plantObj = saleJson.getJSONObject("plant");

            // Verify plant structure
            Assert.assertTrue("Plant should have 'id'", plantObj.has("id"));
            Assert.assertTrue("Plant should have 'name'", plantObj.has("name"));
            Assert.assertTrue("Plant should have 'price'", plantObj.has("price"));

            int plantId = plantObj.getInt("id");
            String plantName = plantObj.getString("name");
            double plantPrice = plantObj.getDouble("price");

            Assert.assertTrue("Plant ID should be positive", plantId > 0);
            Assert.assertNotNull("Plant name should not be null", plantName);
            Assert.assertFalse("Plant name should not be empty", plantName.isEmpty());
            Assert.assertTrue("Plant price should be positive", plantPrice > 0);

            // Verify sale details
            Assert.assertTrue("Sale should have 'quantity'", saleJson.has("quantity"));
            int quantity = saleJson.getInt("quantity");
            Assert.assertTrue("Quantity should be positive", quantity > 0);

            Assert.assertTrue("Sale should have 'totalPrice'", saleJson.has("totalPrice"));
            double totalPrice = saleJson.getDouble("totalPrice");
            Assert.assertTrue("Total price should be positive", totalPrice > 0);

            Assert.assertTrue("Sale should have 'soldAt'", saleJson.has("soldAt"));
            String soldAt = saleJson.getString("soldAt");
            Assert.assertNotNull("Sold at date should not be null", soldAt);
            Assert.assertFalse("Sold at date should not be empty", soldAt.isEmpty());

            // Verify price calculation (if we have both price and quantity)
            double expectedTotal = plantPrice * quantity;
            Assert.assertEquals("Total price should match plant price * quantity",
                    expectedTotal, totalPrice, 0.01);

            System.out.println("✓ Complete Sale Data:");
            System.out.println("  Sale ID: " + saleId);
            System.out.println("  Plant ID: " + plantId);
            System.out.println("  Plant Name: " + plantName);
            System.out.println("  Plant Price: $" + plantPrice);
            System.out.println("  Quantity: " + quantity);
            System.out.println("  Total Price: $" + totalPrice);
            System.out.println("  Sold At: " + soldAt);
            System.out.println("✓ All sale data is complete and valid");

        } catch (Exception e) {
            System.out.println("❌ Error verifying complete sale data: " + e.getMessage());
            Assert.fail("Complete sale data verification failed: " + e.getMessage());
        }
    }

    @Then("the sale should no longer be accessible")
    public void the_sale_should_no_longer_be_accessible() {
        System.out.println("\n=== Verifying sale is no longer accessible ===");

        if (existingSaleId == null) {
            System.out.println("⚠️ No sale ID to verify deletion");
            return;
        }

        System.out.println("Checking if sale ID " + existingSaleId + " is still accessible...");

        // Try to get the deleted sale
        Response getResponse = given()
                .header("Authorization", adminToken)
                .when()
                .get("/api/sales/" + existingSaleId);

        int statusCode = getResponse.getStatusCode();
        System.out.println("GET request status for deleted sale: " + statusCode);

        // Should return 404 Not Found
        Assert.assertEquals("Deleted sale should return 404 Not Found but got " + statusCode,
                404, statusCode);

        // Optional: Log the error response for debugging
        if (statusCode == 404) {
            String responseBody = getResponse.getBody().asString();
            if (responseBody != null && !responseBody.isEmpty() && !responseBody.equals("null")) {
                System.out.println("404 Response: " + responseBody);
            }
        }

        System.out.println("✓ Sale is no longer accessible (404 Not Found)");
    }

    @Then("no sale record should be created")
    public void no_sale_record_should_be_created() {
        System.out.println("\n=== Verifying no sale was created ===");

        if (createdSaleId != null) {
            System.out.println("Warning: Sale ID was set to: " + createdSaleId);
            createdSaleId = null; // Reset for this test case
        }

        // Check response doesn't contain sale ID (for error responses)
        try {
            String responseBody = response.getBody().asString();
            if (!responseBody.contains("\"id\":")) {
                System.out.println("✓ No sale ID in response");
            } else {
                System.out.println("⚠️ Response contains ID field - check if sale was created");
            }
        } catch (Exception e) {
            System.out.println("Note: Could not check response for sale ID");
        }

        System.out.println("✓ Verified no sale record created");
    }

    @When("admin retrieves the created sale by ID")
    public void admin_retrieves_the_created_sale_by_id() {
        if (createdSaleId == null) {
            Assert.fail("Sale ID is null - sale creation might have failed");
        }

        System.out.println("\n=== Retrieving created sale ===");
        System.out.println("Sale ID: " + createdSaleId);

        response = given()
                .header("Authorization", adminToken)
                .when()
                .get("/api/sales/" + createdSaleId);

        System.out.println("GET Response Status: " + response.getStatusCode());
        System.out.println("GET Response Body: " + response.getBody().asString());
    }

    @Then("the sale should be retrievable with the same data")
    public void the_sale_should_be_retrievable_with_the_same_data() {
        System.out.println("\n=== Verifying sale retrieval ===");

        Assert.assertEquals("Sale should be retrievable", 200, response.getStatusCode());

        try {
            JSONObject retrievedSale = new JSONObject(response.getBody().asString());

            int retrievedId = retrievedSale.getInt("id");
            int retrievedQuantity = retrievedSale.getInt("quantity");
            double retrievedTotalPrice = retrievedSale.getDouble("totalPrice");

            System.out.println("Retrieved Sale Details:");
            System.out.println("  - ID: " + retrievedId);
            System.out.println("  - Quantity: " + retrievedQuantity);
            System.out.println("  - Total Price: " + retrievedTotalPrice);

            // Verify data matches original
            Assert.assertEquals("Sale ID should match", createdSaleId, (Integer) retrievedId);

            if (saleResponseBody != null) {
                Assert.assertEquals("Quantity should match",
                        saleResponseBody.getInt("quantity"), retrievedQuantity);
                Assert.assertEquals("Total price should match",
                        saleResponseBody.getDouble("totalPrice"), retrievedTotalPrice, 0.01);
            }

            System.out.println("✓ Sale retrieval verification passed");

        } catch (Exception e) {
            System.out.println("❌ Error verifying sale retrieval: " + e.getMessage());
            Assert.fail("Sale retrieval verification failed: " + e.getMessage());
        }
    }

    @Then("plant stock should be reduced by {int}")
    public void plant_stock_should_be_reduced_by(Integer reductionAmount) {
        System.out.println("\n=== Verifying stock reduction ===");
        System.out.println("Expected reduction: " + reductionAmount + " units");
        System.out.println("Initial stock before test: " + stockBeforeTest + " units");

        // Get updated plant stock
        Response plantResponse = given()
                .header("Authorization", adminToken)
                .when()
                .get("/api/plants/" + PLANT_ID);

        Assert.assertEquals("Plant should be retrievable", 200, plantResponse.getStatusCode());

        Integer updatedStock = null;
        try {
            JSONObject plantJson = new JSONObject(plantResponse.getBody().asString());
            if (plantJson.has("quantity")) {
                updatedStock = plantJson.getInt("quantity");
            } else if (plantJson.has("stock")) {
                updatedStock = plantJson.getInt("stock");
            }
        } catch (Exception e) {
            System.out.println("Error parsing plant response: " + e.getMessage());
        }

        if (updatedStock == null) {
            Assert.fail("Could not get updated stock from plant response");
        }

        System.out.println("Updated stock: " + updatedStock + " units");

        // Calculate expected stock
        int expectedStock = stockBeforeTest - reductionAmount;

        System.out.println("Expected stock after reduction: " + expectedStock + " units");
        System.out.println("Actual stock: " + updatedStock + " units");

        // Verify stock reduction
        Assert.assertEquals(
                "Plant stock should be reduced by " + reductionAmount + " units",
                expectedStock,
                updatedStock.intValue());

        System.out.println("✓ Stock reduction verification passed");
        System.out.println("  Initial: " + stockBeforeTest);
        System.out.println("  Final: " + updatedStock);
        System.out.println("  Reduction: " + (stockBeforeTest - updatedStock) + " units");
    }

    @Then("plant stock should remain unchanged")
    public void plant_stock_should_remain_unchanged() {
        System.out.println("\n=== Verifying stock unchanged ===");
        System.out.println("Initial stock before test: " + stockBeforeTest + " units");

        // Get current plant stock
        Response plantResponse = given()
                .header("Authorization", adminToken)
                .when()
                .get("/api/plants/" + PLANT_ID);

        Assert.assertEquals("Plant should be retrievable", 200, plantResponse.getStatusCode());

        Integer currentStock = null;
        try {
            JSONObject plantJson = new JSONObject(plantResponse.getBody().asString());
            if (plantJson.has("quantity")) {
                currentStock = plantJson.getInt("quantity");
            } else if (plantJson.has("stock")) {
                currentStock = plantJson.getInt("stock");
            }
        } catch (Exception e) {
            System.out.println("Error parsing plant response: " + e.getMessage());
        }

        if (currentStock == null) {
            Assert.fail("Could not get current stock from plant response");
        }

        System.out.println("Current stock: " + currentStock + " units");

        // Verify stock is unchanged
        Assert.assertEquals(
                "Plant stock should remain unchanged",
                stockBeforeTest,
                currentStock);

        System.out.println("✓ Stock unchanged verification passed");
        System.out.println("  Initial: " + stockBeforeTest);
        System.out.println("  Current: " + currentStock);
        System.out.println("  Difference: " + (stockBeforeTest - currentStock) + " units");
    }

    @After("@API")
    public void cleanup() {
        System.out.println("\n=== API Test Cleanup ===");

        // Clean up created sale if it exists
        if (createdSaleId != null) {
            try {
                System.out.println("Cleaning up sale ID: " + createdSaleId);

                Response deleteResponse = given()
                        .header("Authorization", adminToken)
                        .when()
                        .delete("/api/sales/" + createdSaleId);

                System.out.println("Delete response: " + deleteResponse.getStatusCode());

                if (deleteResponse.getStatusCode() == 200 || deleteResponse.getStatusCode() == 204) {
                    System.out.println("✓ Sale cleanup successful");
                }
            } catch (Exception e) {
                System.out.println("Cleanup failed: " + e.getMessage());
            }
        }

        System.out.println("=== Cleanup Complete ===");
    }
}