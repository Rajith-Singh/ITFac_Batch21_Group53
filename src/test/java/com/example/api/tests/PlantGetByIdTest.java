package com.example.api.tests;

import com.example.api.base.BaseTest;
import com.example.api.constants.ApiEndpoints;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class PlantGetByIdTest extends BaseTest {

    @Test
    public void TC_API_PLANTS_USER_016_verifyGetPlantById() {
        int plantId = 17; // change if needed

        RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + userToken)
        .when()
                .get(ApiEndpoints.PLANTS + "/" + plantId)
        .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("id", equalTo(plantId))
                .body("name", notNullValue())
                .body("price", allOf(greaterThan(0f)))          // fix for float values
                .body("quantity", greaterThanOrEqualTo(0))
                .body("categoryId", greaterThanOrEqualTo(0));
    }
}


