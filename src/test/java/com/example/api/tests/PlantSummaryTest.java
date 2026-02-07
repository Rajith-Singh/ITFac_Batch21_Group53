package com.example.api.tests;

import com.example.api.base.BaseTest;
import com.example.api.constants.ApiEndpoints;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class PlantSummaryTest extends BaseTest {

    @Test
    public void TC_API_PLANTS_USER_014_verifyUserCanAccessPlantSummary() {

        RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + userToken)
                .when()
                .get(ApiEndpoints.PLANTS_SUMMARY)
                .then()
                .spec(responseSpec)
                .statusCode(200)

                .body("totalPlants", notNullValue())
                .body("lowStockPlants", notNullValue())
                .body("totalPlants", greaterThanOrEqualTo(0))
                .body("lowStockPlants", greaterThanOrEqualTo(0))

                .body("$", not(hasKey("adminData")))
                .body("$", not(hasKey("internalStats")));
    }
}
