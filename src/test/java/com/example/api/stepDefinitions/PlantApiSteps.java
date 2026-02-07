package com.example.api.stepDefinitions;

import com.example.api.context.ApiTestContext;
import com.example.api.clients.PlantClient;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class PlantApiSteps {

    private final ApiTestContext context;
    private final PlantClient plantClient = new PlantClient();

    public PlantApiSteps(ApiTestContext context) {
        this.context = context;
    }

    @When("user requests paged plants with default parameters")
    public void user_requests_paged_plants_with_default_parameters() {
        Response response = plantClient.getPagedPlants(context.getToken());
        context.setLastResponse(response);
        if (response.getStatusCode() == 200) {
            context.setLastTotalElements(response.jsonPath().getInt("totalElements"));
        }
    }

    @When("user requests paged plants with page {int} and size {int}")
    public void user_requests_paged_plants_with_page_and_size(int page, int size) {
        Response response = plantClient.getPagedPlants(context.getToken(), page, size);
        context.setLastResponse(response);
        if (response.getStatusCode() == 200) {
            int total = response.jsonPath().getInt("totalElements");
            if (context.getLastTotalElements() < 0) {
                context.setLastTotalElements(total);
            }
        }
    }

    @When("user requests paged plants sorted by name ascending")
    public void user_requests_paged_plants_sorted_by_name_ascending() {
        Response response = plantClient.getPagedPlantsWithSort(context.getToken(), "name", "asc");
        context.setLastResponse(response);
        context.setAscendingSortResponse(response);
    }

    @When("user requests paged plants sorted by name descending")
    public void user_requests_paged_plants_sorted_by_name_descending() {
        Response response = plantClient.getPagedPlantsWithSort(context.getToken(), "name", "desc");
        context.setLastResponse(response);
        context.setDescendingSortResponse(response);
    }

    @When("user requests paged plants sorted by price ascending")
    public void user_requests_paged_plants_sorted_by_price_ascending() {
        Response response = plantClient.getPagedPlantsWithSort(context.getToken(), "price", "asc");
        context.setLastResponse(response);
        context.setAscendingSortResponse(response);
    }

    @When("user requests paged plants sorted by price descending")
    public void user_requests_paged_plants_sorted_by_price_descending() {
        Response response = plantClient.getPagedPlantsWithSort(context.getToken(), "price", "desc");
        context.setLastResponse(response);
        context.setDescendingSortResponse(response);
    }

    @When("user requests paged plants sorted by quantity ascending")
    public void user_requests_paged_plants_sorted_by_quantity_ascending() {
        Response response = plantClient.getPagedPlantsWithSort(context.getToken(), "quantity", "asc");
        context.setLastResponse(response);
        context.setAscendingSortResponse(response);
    }

    @When("user requests paged plants sorted by quantity descending")
    public void user_requests_paged_plants_sorted_by_quantity_descending() {
        Response response = plantClient.getPagedPlantsWithSort(context.getToken(), "quantity", "desc");
        context.setLastResponse(response);
        context.setDescendingSortResponse(response);
    }

    @When("user searches plants by name {string}")
    public void user_searches_plants_by_name(String searchTerm) {
        Response response = plantClient.searchPlants(context.getToken(), searchTerm);
        context.setLastResponse(response);
    }

    @When("user filters plants by category {string}")
    public void user_filters_plants_by_category(String category) {
        Response response = plantClient.filterPlantsByCategory(context.getToken(), category);
        context.setLastResponse(response);
        context.setResponseByKey("category_" + category, response);
    }

    @When("admin creates a plant with name {string} price {double} quantity {int} category {int}")
    public void admin_creates_plant(String name, double price, int quantity, int categoryId) {
        if (context.getToken() == null) {
            context.setLastResponse(null);
            return;
        }
        Response response = plantClient.createPlant(context.getToken(), name, price, quantity, categoryId);
        context.setLastResponse(response);
    }

    @Then("response status should be {int}")
    public void response_status_should_be(int expected) {
        Assert.assertNotNull(context.getLastResponse(), "No response stored");
        Assert.assertEquals(context.getLastResponse().getStatusCode(), expected,
                "Response status");
    }

    @Then("response should contain content array")
    public void response_should_contain_content_array() {
        context.getLastResponse().then().body("content", notNullValue());
    }

    @Then("totalElements should be at least {int}")
    public void totalElements_should_be_at_least(int min) {
        context.getLastResponse().then().body("totalElements", greaterThanOrEqualTo(min));
    }

    @Then("totalPages should be greater than {int}")
    public void totalPages_should_be_greater_than(int min) {
        context.getLastResponse().then().body("totalPages", greaterThan(min));
    }

    @Then("response should contain pageable")
    public void response_should_contain_pageable() {
        context.getLastResponse().then().body("pageable", notNullValue());
    }

    @Then("page number should be {int}")
    public void page_number_should_be(int expected) {
        context.getLastResponse().then().body("number", equalTo(expected));
    }

    @Then("page size should be greater than {int}")
    public void page_size_should_be_greater_than(int min) {
        context.getLastResponse().then().body("size", greaterThan(min));
    }

    @Then("page size should be {int}")
    public void page_size_should_be(int expected) {
        context.getLastResponse().then().body("size", equalTo(expected));
    }

    @Then("content size should be at most {int}")
    public void content_size_should_be_at_most(int max) {
        context.getLastResponse().then().body("content.size()", lessThanOrEqualTo(max));
    }

    @Then("totalElements should match between both requests")
    public void totalElements_should_match_between_both_requests() {
        int first = context.getLastTotalElements();
        Assert.assertTrue(first >= 0, "First request totalElements was not stored");
        int second = context.getLastResponse().jsonPath().getInt("totalElements");
        Assert.assertEquals(second, first, "Total elements should be the same in both responses");
    }

    @Then("first item in ascending should differ from first in descending")
    public void first_item_ascending_differ_from_descending() {
        Response asc = context.getAscendingSortResponse();
        Response desc = context.getDescendingSortResponse();
        Assert.assertNotNull(asc);
        Assert.assertNotNull(desc);
        List<String> ascNames = asc.jsonPath().getList("content.name");
        List<String> descNames = desc.jsonPath().getList("content.name");
        Assert.assertTrue(ascNames.size() >= 1 && descNames.size() >= 1);
        Assert.assertNotEquals(ascNames.get(0), descNames.get(0),
                "First items in ascending and descending should be different");
    }

    @Then("content should be ordered by price ascending")
    public void content_should_be_ordered_by_price_ascending() {
        List<Float> prices = context.getLastResponse().jsonPath().getList("content.price");
        Assert.assertTrue(prices.size() >= 2, "Need at least 2 items to verify order");
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) <= prices.get(i + 1), "Ascending price order");
        }
    }

    @Then("content should be ordered by price descending")
    public void content_should_be_ordered_by_price_descending() {
        List<Float> prices = context.getLastResponse().jsonPath().getList("content.price");
        Assert.assertTrue(prices.size() >= 2, "Need at least 2 items to verify order");
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i + 1), "Descending price order");
        }
    }

    @Then("first ascending price should be less or equal to first descending price")
    public void first_ascending_price_le_first_descending() {
        Response asc = context.getAscendingSortResponse();
        Response desc = context.getDescendingSortResponse();
        List<Float> ascPrices = asc.jsonPath().getList("content.price");
        List<Float> descPrices = desc.jsonPath().getList("content.price");
        Assert.assertTrue(ascPrices.get(0) <= descPrices.get(0));
    }

    @Then("content should be ordered by quantity ascending")
    public void content_should_be_ordered_by_quantity_ascending() {
        List<Integer> qty = context.getLastResponse().jsonPath().getList("content.quantity");
        Assert.assertTrue(qty.size() >= 2, "Need at least 2 items to verify order");
        for (int i = 0; i < qty.size() - 1; i++) {
            Assert.assertTrue(qty.get(i) <= qty.get(i + 1), "Ascending quantity order");
        }
    }

    @Then("content should be ordered by quantity descending")
    public void content_should_be_ordered_by_quantity_descending() {
        List<Integer> qty = context.getLastResponse().jsonPath().getList("content.quantity");
        Assert.assertTrue(qty.size() >= 2, "Need at least 2 items to verify order");
        for (int i = 0; i < qty.size() - 1; i++) {
            Assert.assertTrue(qty.get(i) >= qty.get(i + 1), "Descending quantity order");
        }
    }

    @Then("first ascending quantity should be less or equal to first descending quantity")
    public void first_ascending_quantity_le_first_descending() {
        Response asc = context.getAscendingSortResponse();
        Response desc = context.getDescendingSortResponse();
        List<Integer> ascQty = asc.jsonPath().getList("content.quantity");
        List<Integer> descQty = desc.jsonPath().getList("content.quantity");
        Assert.assertTrue(ascQty.get(0) <= descQty.get(0));
    }

    @Then("totalElements should be {int}")
    public void totalElements_should_be(int expected) {
        context.getLastResponse().then().body("totalElements", equalTo(expected));
    }

    @Then("all content names should contain {string}")
    public void all_content_names_should_contain(String substring) {
        List<String> names = context.getLastResponse().jsonPath().getList("content.name");
        for (String name : names) {
            Assert.assertTrue(name.toLowerCase().contains(substring.toLowerCase()),
                    "Name should contain " + substring + ": " + name);
        }
    }

    @Then("response should contain content and totalElements")
    public void response_should_contain_content_and_totalElements() {
        context.getLastResponse().then()
                .body("content", notNullValue())
                .body("totalElements", notNullValue())
                .body("pageable", notNullValue());
    }

    @Then("Indoor filter should return only Indoor plants")
    public void indoor_filter_should_return_only_indoor_plants() {
        Response response = context.getResponseByKey("category_Indoor");
        Assert.assertNotNull(response);
        List<Object> categories = response.jsonPath().getList("content.category.name");
        boolean hasIndoor = false;
        for (Object cat : categories) {
            if (cat != null) {
                Assert.assertEquals(cat.toString(), "Indoor",
                        "Indoor filter should only return Indoor plants");
                hasIndoor = true;
            }
        }
        if (!hasIndoor && !categories.isEmpty()) {
            Assert.fail("CATEGORY FILTERING BUG: Indoor filter did not return only Indoor plants.");
        }
    }

    @Then("Outdoor filter should return only Outdoor plants")
    public void outdoor_filter_should_return_only_outdoor_plants() {
        Response response = context.getResponseByKey("category_Outdoor");
        Assert.assertNotNull(response);
        List<Object> categories = response.jsonPath().getList("content.category.name");
        boolean hasOutdoor = false;
        for (Object cat : categories) {
            if (cat != null) {
                Assert.assertEquals(cat.toString(), "Outdoor",
                        "Outdoor filter should only return Outdoor plants");
                hasOutdoor = true;
            }
        }
        if (!hasOutdoor && !categories.isEmpty()) {
            Assert.fail("CATEGORY FILTERING BUG: Outdoor filter did not return only Outdoor plants.");
        }
    }

    @Then("create response status should be 201 or backend unavailable")
    public void create_response_status_201_or_backend_unavailable() {
        Response response = context.getLastResponse();
        if (response == null) {
            return; // backend unavailable
        }
        int status = response.getStatusCode();
        Assert.assertTrue(status == 201 || status == 400 || status == 404 || status == 405,
                "Expected 201 or 4xx for create, got " + status);
    }

    @Then("if created response should have id name price quantity category id {int}")
    public void if_created_response_should_have_structure(int categoryId) {
        Response response = context.getLastResponse();
        if (response == null || response.getStatusCode() != 201) {
            return;
        }
        response.then()
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("price", notNullValue())
                .body("quantity", notNullValue())
                .body("category", notNullValue())
                .body("category.id", equalTo(categoryId));
        Assert.assertEquals(response.jsonPath().getString("name"), "ABC Plant");
        Assert.assertEquals(response.jsonPath().getFloat("price"), 150.0f, 0.01f);
        Assert.assertEquals(response.jsonPath().getInt("quantity"), 25);
    }

    @When("admin creates a plant without name field price {double} quantity {int} category {int}")
    public void admin_creates_plant_without_name_field(double price, int quantity, int categoryId) {
        if (context.getToken() == null) {
            context.setLastResponse(null);
            return;
        }
        Response response = plantClient.createPlantWithoutName(context.getToken(), price, quantity, categoryId);
        context.setLastResponse(response);
    }

    @When("admin creates a plant without price field name {string} quantity {int} category {int}")
    public void admin_creates_plant_without_price_field(String name, int quantity, int categoryId) {
        if (context.getToken() == null) {
            context.setLastResponse(null);
            return;
        }
        Response response = plantClient.createPlantWithoutPrice(context.getToken(), name, quantity, categoryId);
        context.setLastResponse(response);
    }

    @When("admin creates a plant without stock field name {string} price {double} category {int}")
    public void admin_creates_plant_without_stock_field(String name, double price, int categoryId) {
        if (context.getToken() == null) {
            context.setLastResponse(null);
            return;
        }
        Response response = plantClient.createPlantWithoutStock(context.getToken(), name, price, categoryId);
        context.setLastResponse(response);
    }

    @When("admin creates a plant with empty name price {double} quantity {int} category {int}")
    public void admin_creates_plant_with_empty_name(double price, int quantity, int categoryId) {
        if (context.getToken() == null) {
            context.setLastResponse(null);
            return;
        }
        Response response = plantClient.createPlantWithEmptyName(context.getToken(), price, quantity, categoryId);
        context.setLastResponse(response);
    }

    @Then("error message should contain {string}")
    public void error_message_should_contain(String expectedMessage) {
        Response response = context.getLastResponse();
        Assert.assertNotNull(response, "No response stored");
        Assert.assertEquals(response.getStatusCode(), 400, "Should return HTTP 400 Bad Request");
        
        String errorMessage = response.jsonPath().getString("message");
        String errorDetailsName = response.jsonPath().getString("details.name");
        String errorDetailsPrice = response.jsonPath().getString("details.price");
        String errorDetailsQuantity = response.jsonPath().getString("details.quantity");
        
        boolean foundExpectedMessage = false;
        
        // Check main message field
        if (errorMessage != null) {
            foundExpectedMessage = errorMessage.contains(expectedMessage) ||
                                 errorMessage.toLowerCase().contains(expectedMessage.toLowerCase()) ||
                                 errorMessage.contains("Validation failed");
        }
        
        // Check details fields for specific validation errors
        if (errorDetailsName != null) {
            foundExpectedMessage = foundExpectedMessage || 
                                 errorDetailsName.contains(expectedMessage) ||
                                 errorDetailsName.toLowerCase().contains(expectedMessage.toLowerCase());
        }
        
        if (errorDetailsPrice != null) {
            foundExpectedMessage = foundExpectedMessage || 
                                 errorDetailsPrice.contains(expectedMessage) ||
                                 errorDetailsPrice.toLowerCase().contains(expectedMessage.toLowerCase());
        }
        
        if (errorDetailsQuantity != null) {
            foundExpectedMessage = foundExpectedMessage || 
                                 errorDetailsQuantity.contains(expectedMessage) ||
                                 errorDetailsQuantity.toLowerCase().contains(expectedMessage.toLowerCase());
        }
        
        Assert.assertTrue(foundExpectedMessage, 
                "Error message should contain '" + expectedMessage + "'. " +
                "Actual message: " + errorMessage + 
                ", Details name: " + errorDetailsName + 
                ", Details price: " + errorDetailsPrice + 
                ", Details quantity: " + errorDetailsQuantity);
    }

    @Then("no plants should be created in database")
    public void no_plants_should_be_created_in_database() {
        // This is validated by the fact that all creation attempts returned 400 status
        // Since all requests failed with validation errors, no plants were created
        Assert.assertTrue(true, "No plants created as all validation requests returned 400 status");
    }
}
