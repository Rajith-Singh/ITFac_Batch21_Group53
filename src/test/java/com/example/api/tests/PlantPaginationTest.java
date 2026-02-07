package com.example.api.tests;

import com.example.api.base.BaseTest;
import com.example.api.constants.ApiEndpoints;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class PlantPaginationTest extends BaseTest {

    @Test
    public void TC_API_PLANTS_PAGINATION_verifyResponseStructure() {

        RestAssured.given()
                .spec(requestSpec) // Uses your BaseTest requestSpec
                .header("Authorization", "Bearer " + userToken) // JWT token from BaseTest
                .queryParam("page", 1)
                .queryParam("size", 2)
                .queryParam("sort", "string") // As per your request
                .when()
                .get(ApiEndpoints.PLANTS_PAGED)
                .then()
                .spec(responseSpec) // Uses your BaseTest responseSpec
                .statusCode(200) // Expecting success

                // Pagination fields
                .body("totalPages", notNullValue())
                .body("totalElements", notNullValue())
                .body("size", notNullValue())
                .body("number", notNullValue())
                .body("first", notNullValue())
                .body("last", notNullValue())
                .body("numberOfElements", notNullValue())
                .body("empty", notNullValue())

                // Content array checks
                .body("content.size()", greaterThanOrEqualTo(0))
                .body("content[0].id", notNullValue())
                .body("content[0].name", notNullValue())
                .body("content[0].price", notNullValue())
                .body("content[0].quantity", notNullValue())
                .body("content[0].category.id", notNullValue())
                .body("content[0].category.name", notNullValue())
                .body("content[0].category.subCategories", notNullValue())

                // Sort array checks
                .body("sort.size()", greaterThanOrEqualTo(0))
                .body("sort[0].direction", notNullValue())
                .body("sort[0].property", notNullValue())
                .body("sort[0].ascending", notNullValue());
    }
}
