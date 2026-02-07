package com.example.api.tests;

import com.example.api.base.BaseTest;
import com.example.api.constants.ApiEndpoints;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class PlantDeleteTest extends BaseTest {

    @Test
    public void TC_API_PLANTS_ADM_012_verifyDeleteNonExistingPlant() {

        int nonExistingPlantId = 99999;

        RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + userToken)

        .when()
                .delete(ApiEndpoints.PLANTS + "/" + nonExistingPlantId)

        .then()
                .spec(responseSpec)
                .statusCode(404)

                // Validate error response
                .body("message", equalTo("Plant not found"))
                .body("status", equalTo(404))
                .body("error", notNullValue());
    }
}
