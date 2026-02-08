package com.example.api.stepDefinitions;

import com.example.api.config.ApiConfig;
import com.example.api.constants.ApiEndpoints;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PlantApiStepsP {

    private Response response;
    private String token;
    private RequestSpecification requestSpec;

    @Given("I have a valid user token")
    public void iHaveAValidUserToken() {
        RestAssured.baseURI = ApiConfig.getBaseUrl();
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();

        Response loginResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"admin\",\"password\":\"admin123\"}")
                .post(ApiEndpoints.AUTH_LOGIN);

        token = loginResponse.jsonPath().getString("token");
    }

    // --- Plant By Category Steps ---
    @When("I send a GET request to get plants by category ID {int}")
    public void iSendAGETRequestToGetPlantsByCategoryID(int categoryId) {
        response = RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(ApiEndpoints.PLANTS + "/category/" + categoryId);
    }

    @And("the response body should not be empty")
    public void theResponseBodyShouldNotBeEmpty() {
        response.then().body("$", not(empty()));
    }

    @And("all plants in the response should belong to category ID {int}")
    public void allPlantsInTheResponseShouldBelongToCategoryID(int categoryId) {
        response.then().body("category.id", everyItem(equalTo(categoryId)));
    }

    @And("each plant should have valid ID, name, price, and quantity")
    public void eachPlantShouldHaveValidIDNamePriceAndQuantity() {
        response.then()
                .body("[0].id", notNullValue())
                .body("[0].name", notNullValue())
                .body("[0].price", greaterThan(0.0f))
                .body("[0].quantity", greaterThanOrEqualTo(0));
    }

    // --- Plant Delete Steps ---
    @When("I send a DELETE request for a non-existing plant with ID {int}")
    public void iSendADELETERequestForANonExistingPlantWithID(int plantId) {
        response = RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(ApiEndpoints.PLANTS + "/" + plantId);
    }

    @And("the response body should contain message {string}")
    public void theResponseBodyShouldContainMessage(String message) {
        response.then().body("message", equalTo(message));
    }

    @And("the response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        response.then().body("status", equalTo(status));
    }

    @And("the response error should not be null")
    public void theResponseErrorShouldNotBeNull() {
        response.then().body("error", notNullValue());
    }

    // --- Plant Get By ID Steps ---
    @When("I send a GET request to get plant with ID {int}")
    public void iSendAGETRequestToGetPlantWithID(int plantId) {
        response = RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(ApiEndpoints.PLANTS + "/" + plantId);
    }

    @And("the plant ID in response should be {int}")
    public void thePlantIDInResponseShouldBe(int plantId) {
        response.then().body("id", equalTo(plantId));
    }

    @And("the plant name should not be null")
    public void thePlantNameShouldNotBeNull() {
        response.then().body("name", notNullValue());
    }

    @And("the plant price should be greater than 0")
    public void thePlantPriceShouldBeGreaterThan() {
        response.then().body("price", greaterThan(0f));
    }

    @And("the plant quantity should be greater than or equal to 0")
    public void thePlantQuantityShouldBeGreaterThanOrEqualTo() {
        response.then().body("quantity", greaterThanOrEqualTo(0));
    }

    @And("the plant categoryId should be greater than or equal to 0")
    public void thePlantCategoryIdShouldBeGreaterThanOrEqualTo() {
        response.then().body("categoryId", greaterThanOrEqualTo(0));
    }

    // --- Pagination and Sorting Steps ---
    @When("I send a GET request for plants with page {int}, size {int}, and sort by {string} in {string} order")
    public void iSendAGETRequestForPlantsWithPageSizeAndSortByInOrder(int page, int size, String field, String order) {
        response = RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sort", field + "," + order)
                .when()
                .get(ApiEndpoints.PLANTS_PAGED);
    }

    @And("the response should be sorted by {string} in {string} order")
    public void theResponseShouldBeSortedByInOrder(String field, String order) {
        List<?> values = response.jsonPath().getList("content." + field);
        if (order.equalsIgnoreCase("asc")) {
            verifyAscendingOrder(values);
        } else {
            verifyDescendingOrder(values);
        }
    }

    // --- Plant Summary Steps ---
    @When("I send a GET request to get plant summary")
    public void iSendAGETRequestToGetPlantSummary() {
        response = RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(ApiEndpoints.PLANTS_SUMMARY);
    }

    @And("the summary should contain totalPlants and lowStockPlants")
    public void theSummaryShouldContainTotalPlantsAndLowStockPlants() {
        response.then()
                .body("totalPlants", notNullValue())
                .body("lowStockPlants", notNullValue());
    }

    @And("totalPlants and lowStockPlants should be greater than or equal to 0")
    public void totalPlantsAndLowStockPlantsShouldBeGreaterThanOrEqualTo() {
        response.then()
                .body("totalPlants", greaterThanOrEqualTo(0))
                .body("lowStockPlants", greaterThanOrEqualTo(0));
    }

    @And("the response should not contain admin fields {string} or {string}")
    public void theResponseShouldNotContainAdminFieldsOr(String field1, String field2) {
        response.then()
                .body("$", not(hasKey(field1)))
                .body("$", not(hasKey(field2)));
    }

    // --- Plant Update Steps ---
    @When("I send a PUT request to update plant with ID {int} and name {string}")
    public void iSendAPUTRequestToUpdatePlantWithIDAndName(int id, String name) {
        String requestBody = "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"price\": 150,\n" +
                "  \"quantity\": 25,\n" +
                "  \"category\": {\n" +
                "    \"id\": " + id + ",\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"parent\": \"string\",\n" +
                "    \"subCategories\": [\"string\"]\n" +
                "  }\n" +
                "}";

        response = RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .put(ApiEndpoints.UPDATE_PLANT);
    }

    @And("the updated plant ID should be {int}")
    public void theUpdatedPlantIDShouldBe(int id) {
        response.then().body("id", equalTo(id));
    }

    @And("the updated plant name should be {string}")
    public void theUpdatedPlantNameShouldBe(String name) {
        response.then().body("name", equalTo(name));
    }

    @And("the updated plant price should be {int}")
    public void theUpdatedPlantPriceShouldBe(int price) {
        response.then().body("price", equalTo(price));
    }

    @And("the updated plant quantity should be {int}")
    public void theUpdatedPlantQuantityShouldBe(int quantity) {
        response.then().body("quantity", equalTo(quantity));
    }

    @And("the updated plant category ID should be {int}")
    public void theUpdatedPlantCategoryIDShouldBe(int categoryId) {
        response.then().body("category.id", equalTo(categoryId));
    }

    @And("the updated plant category name should be {string}")
    public void theUpdatedPlantCategoryNameShouldBe(String categoryName) {
        response.then().body("category.name", equalTo(categoryName));
    }

    // --- General Assertions ---
    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        response.then().statusCode(statusCode);
    }

    // --- Private Utilities (copied from PlantPaginationSortTest) ---
    private void verifyAscendingOrder(List<?> values) {
        for (int i = 1; i < values.size(); i++) {
            Object previous = values.get(i - 1);
            Object current = values.get(i);
            if (previous instanceof Number && current instanceof Number) {
                Double prevNum = ((Number) previous).doubleValue();
                Double currNum = ((Number) current).doubleValue();
                assertThat(currNum, greaterThanOrEqualTo(prevNum));
            } else if (previous instanceof String && current instanceof String) {
                assertThat((String) current, greaterThanOrEqualTo((String) previous));
            }
        }
    }

    private void verifyDescendingOrder(List<?> values) {
        for (int i = 1; i < values.size(); i++) {
            Object previous = values.get(i - 1);
            Object current = values.get(i);
            if (previous instanceof Number && current instanceof Number) {
                Double prevNum = ((Number) previous).doubleValue();
                Double currNum = ((Number) current).doubleValue();
                assertThat(currNum, lessThanOrEqualTo(prevNum));
            } else if (previous instanceof String && current instanceof String) {
                assertThat((String) current, lessThanOrEqualTo((String) previous));
            }
        }
    }
}