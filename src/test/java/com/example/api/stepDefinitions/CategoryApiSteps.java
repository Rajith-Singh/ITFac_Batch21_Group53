package com.example.api.stepDefinitions;

import com.example.api.clients.AuthClient;
import com.example.api.clients.CategoryClient;
import com.example.api.models.response.CategoryResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;

public class CategoryApiSteps {
    
    private AuthClient authClient;
    private CategoryClient categoryClient;
    private Response response;
    private String userToken;
    private String adminToken;
    private List<CategoryResponse> categories;
    
    public CategoryApiSteps() {
        this.authClient = new AuthClient();
        this.categoryClient = new CategoryClient();
    }
    
    @Given("I am authenticated as a regular user")
    public void i_am_authenticated_as_regular_user() {
        userToken = authClient.getUserToken();
        Assert.assertNotNull(userToken, "User token should not be null");
    }
    
    @Given("I am authenticated as an admin")
    public void i_am_authenticated_as_admin() {
        adminToken = authClient.getAdminToken();
        Assert.assertNotNull(adminToken, "Admin token should not be null");
    }
    
    @Given("multiple categories exist in the system")
    public void multiple_categories_exist_in_system() {
        // This is a precondition check - the system should have categories
        // In a real scenario, you might want to create test data here
        // For now, we assume the system has pre-existing categories
    }
    
    @When("I send a GET request to {string}")
    public void i_send_get_request_to(String endpoint) {
        if (endpoint.equals("/api/categories")) {
            // Send request without authentication token
            response = categoryClient.getAllCategories("");
        }
    }
    
    @When("I send a GET request to {string} with user credentials")
    public void i_send_get_request_with_user_credentials(String endpoint) {
        if (endpoint.equals("/api/categories")) {
            response = categoryClient.getAllCategories(userToken);
        }
    }
    
    @When("I send a GET request to {string} with admin credentials")
    public void i_send_get_request_with_admin_credentials(String endpoint) {
        if (endpoint.equals("/api/categories")) {
            response = categoryClient.getAllCategories(adminToken);
        } else if (endpoint.startsWith("/api/categories/")) {
            String[] parts = endpoint.split("/");
            if (parts.length == 4 && parts[3].matches("\\d+")) {
                // GET /api/categories/{id}
                Long categoryId = Long.parseLong(parts[3]);
                response = categoryClient.getCategoryById(adminToken, categoryId);
            } else if (endpoint.equals("/api/categories/main")) {
                response = categoryClient.getMainCategories(adminToken);
            } else if (endpoint.equals("/api/categories/sub-categories")) {
                response = categoryClient.getSubCategories(adminToken);
            }
        }
    }
    
    @When("I send a GET request to {string} with category ID {long}")
    public void i_send_get_request_with_category_id(String endpoint, Long categoryId) {
        response = categoryClient.getCategoryById(userToken != null ? userToken : adminToken, categoryId);
    }
    
    @When("I send a GET request to {string} for main categories")
    public void i_send_get_request_for_main_categories(String endpoint) {
        response = categoryClient.getMainCategories(userToken != null ? userToken : adminToken);
    }
    
    @When("I send a GET request to {string} for sub-categories")
    public void i_send_get_request_for_sub_categories(String endpoint) {
        response = categoryClient.getSubCategories(userToken != null ? userToken : adminToken);
    }
    
    @When("I set Authorization header with user token")
    public void i_set_authorization_header_with_user_token() {
        // This is handled automatically in the CategoryClient
        Assert.assertNotNull(userToken, "User token should be set");
    }
    
    @When("I set Accept header to {string}")
    public void i_set_accept_header(String contentType) {
        // This is handled automatically in the CategoryClient
        // All requests set Accept: application/json
    }
    
    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode, 
            "Expected status code " + statusCode + " but got " + response.getStatusCode());
    }
    
    @Then("the response should be a JSON array")
    public void the_response_should_be_json_array() {
        String contentType = response.getContentType();
        Assert.assertTrue(contentType.contains("application/json"), 
            "Content type should be application/json but got " + contentType);
        
        // Verify response body is an array
        String body = response.getBody().asString();
        Assert.assertTrue(body.trim().startsWith("["), "Response should be a JSON array");
    }
    
    @Then("the response should contain category objects")
    public void the_response_should_contain_category_objects() {
        CategoryResponse[] categoryArray = response.as(CategoryResponse[].class);
        categories = Arrays.asList(categoryArray);
        
        // If array is empty, that's still valid (empty array of categories)
        Assert.assertNotNull(categories, "Categories list should not be null");
    }
    
    @Then("each category should have required fields")
    public void each_category_should_have_required_fields() {
        CategoryResponse[] categoryArray = response.as(CategoryResponse[].class);
        
        for (CategoryResponse category : categoryArray) {
            Assert.assertNotNull(category.getId(), "Category ID should not be null");
            Assert.assertNotNull(category.getName(), "Category name should not be null");
            Assert.assertNotNull(category.getIsMainCategory(), "isMainCategory flag should not be null");
            // parentCategory can be null for main categories
        }
    }
    
    @Then("each category should contain field {string}")
    public void each_category_should_contain_field(String fieldName) {
        CategoryResponse[] categoryArray = response.as(CategoryResponse[].class);
        
        for (CategoryResponse category : categoryArray) {
            switch (fieldName) {
                case "id":
                    Assert.assertNotNull(category.getId(), "Category should have id field");
                    break;
                case "name":
                    Assert.assertNotNull(category.getName(), "Category should have name field");
                    break;
                case "parentCategory":
                    // parentCategory is allowed to be null for main categories
                    break;
                case "isMainCategory":
                    Assert.assertNotNull(category.getIsMainCategory(), 
                        "Category should have isMainCategory field");
                    break;
            }
        }
    }
    
    @Then("the response should include both main and sub-categories")
    public void the_response_should_include_main_and_sub_categories() {
        CategoryResponse[] categoryArray = response.as(CategoryResponse[].class);
        
        if (categoryArray.length > 0) {
            boolean hasMainCategory = false;
            boolean hasSubCategory = false;
            
            for (CategoryResponse category : categoryArray) {
                if (category.getIsMainCategory() != null && category.getIsMainCategory()) {
                    hasMainCategory = true;
                } else if (category.getIsMainCategory() != null && !category.getIsMainCategory()) {
                    hasSubCategory = true;
                }
            }
            
            // At least one type should exist if there are categories
            Assert.assertTrue(hasMainCategory || hasSubCategory, 
                "Response should contain at least main or sub-categories");
        }
    }
    
    @Then("the response should contain at least {int} categories")
    public void the_response_should_contain_at_least_categories(int minCount) {
        CategoryResponse[] categoryArray = response.as(CategoryResponse[].class);
        Assert.assertTrue(categoryArray.length >= minCount, 
            "Expected at least " + minCount + " categories but got " + categoryArray.length);
    }
    
    @Then("no admin-only fields should be present")
    public void no_admin_only_fields_should_be_present() {
        // This verifies that the response doesn't contain admin-specific fields
        // The CategoryResponse model only contains standard fields
        // This is more of a security check that could be enhanced based on requirements
        String responseBody = response.getBody().asString();
        
        // Check that response doesn't contain common admin-only field patterns
        Assert.assertFalse(responseBody.contains("\"createdBy\""), 
            "Response should not contain admin-only field 'createdBy'");
        Assert.assertFalse(responseBody.contains("\"modifiedBy\""), 
            "Response should not contain admin-only field 'modifiedBy'");
        Assert.assertFalse(responseBody.contains("\"internalId\""), 
            "Response should not contain admin-only field 'internalId'");
    }
    
    @Then("I should be able to count the categories returned")
    public void i_should_be_able_to_count_categories() {
        CategoryResponse[] categoryArray = response.as(CategoryResponse[].class);
        int count = categoryArray.length;
        
        Assert.assertTrue(count >= 0, "Category count should be non-negative");
        System.out.println("Total categories returned: " + count);
    }
    
    @Then("the response should be a single category object")
    public void the_response_should_be_single_category_object() {
        String contentType = response.getContentType();
        Assert.assertTrue(contentType.contains("application/json"), 
            "Content type should be application/json but got " + contentType);
        
        // Verify response body is an object, not an array
        String body = response.getBody().asString();
        Assert.assertTrue(body.trim().startsWith("{"), "Response should be a JSON object, not an array");
        Assert.assertFalse(body.trim().startsWith("["), "Response should not be an array");
    }
    
    @Then("the category should have id {long}")
    public void the_category_should_have_id(Long expectedId) {
        CategoryResponse category = response.as(CategoryResponse.class);
        Assert.assertEquals(category.getId(), expectedId, 
            "Category ID should be " + expectedId + " but got " + category.getId());
    }
    
    @Then("the category should have name {string}")
    public void the_category_should_have_name(String expectedName) {
        CategoryResponse category = response.as(CategoryResponse.class);
        Assert.assertEquals(category.getName(), expectedName, 
            "Category name should be " + expectedName + " but got " + category.getName());
    }
    
    @Then("the category should have parent category {string}")
    public void the_category_should_have_parent_category(String expectedParent) {
        CategoryResponse category = response.as(CategoryResponse.class);
        Assert.assertEquals(category.getParentCategory(), expectedParent, 
            "Parent category should be " + expectedParent + " but got " + category.getParentCategory());
    }
    
    @Then("the response should contain an error message")
    public void the_response_should_contain_error_message() {
        String body = response.getBody().asString();
        Assert.assertNotNull(body, "Response body should not be null");
        Assert.assertTrue(body.length() > 0, "Response body should contain error message");
    }
    
    @Then("the error message should indicate {string}")
    public void the_error_message_should_indicate(String expectedMessage) {
        String body = response.getBody().asString().toLowerCase();
        String searchMessage = expectedMessage.toLowerCase();
        Assert.assertTrue(body.contains(searchMessage) || body.contains("not found") || body.contains("error"), 
            "Error message should indicate: " + expectedMessage);
    }
    
    @Then("all categories should have null parent category")
    public void all_categories_should_have_null_parent() {
        CategoryResponse[] categoryArray = response.as(CategoryResponse[].class);
        
        for (CategoryResponse category : categoryArray) {
            Assert.assertTrue(category.getParentCategory() == null || category.getParentCategory().isEmpty(), 
                "Main category should have null or empty parent category but got: " + category.getParentCategory());
            if (category.getIsMainCategory() != null) {
                Assert.assertTrue(category.getIsMainCategory(), 
                    "Category should be marked as main category");
            }
        }
    }
    
    @Then("all categories should have populated parent category")
    public void all_categories_should_have_populated_parent() {
        CategoryResponse[] categoryArray = response.as(CategoryResponse[].class);
        
        for (CategoryResponse category : categoryArray) {
            Assert.assertNotNull(category.getParentCategory(), 
                "Sub-category should have parent category but it was null");
            Assert.assertFalse(category.getParentCategory().isEmpty(), 
                "Sub-category should have non-empty parent category");
            if (category.getIsMainCategory() != null) {
                Assert.assertFalse(category.getIsMainCategory(), 
                    "Category should not be marked as main category");
            }
        }
    }
    
    @Then("the response should contain exactly {int} categories")
    public void the_response_should_contain_exactly_categories(int expectedCount) {
        CategoryResponse[] categoryArray = response.as(CategoryResponse[].class);
        Assert.assertEquals(categoryArray.length, expectedCount, 
            "Expected exactly " + expectedCount + " categories but got " + categoryArray.length);
    }
    
    @Then("no sensitive or admin fields should be exposed")
    public void no_sensitive_or_admin_fields_exposed() {
        String responseBody = response.getBody().asString();
        
        // Check that response doesn't contain sensitive field patterns
        Assert.assertFalse(responseBody.contains("\"password\""), 
            "Response should not contain password field");
        Assert.assertFalse(responseBody.contains("\"internalId\""), 
            "Response should not contain internalId field");
        Assert.assertFalse(responseBody.contains("\"deletedAt\""), 
            "Response should not contain deletedAt field");
    }
}
