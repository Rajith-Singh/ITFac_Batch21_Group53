package com.example.api.tests;

import com.example.api.base.BaseTest;
import com.example.api.constants.ApiEndpoints;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class PlantByCategoryTest extends BaseTest {

    public PlantByCategoryTest() {
    }

    @Test
    public void TC_API_PLANTS_USER_015_verifyGetPlantsByCategoryId() {

        int categoryId = 5;

        RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + userToken)
        .when()
                .get(ApiEndpoints.PLANTS + "/category/" + categoryId)
        .then()
                .spec(responseSpec)
                .statusCode(200)

                .body("$", not(empty()))

                .body("[0].id", notNullValue())
                .body("[0].name", notNullValue())
                .body("[0].price", greaterThan(0.0f))
                .body("[0].quantity", greaterThanOrEqualTo(0))

                .body("[0].category.id", equalTo(categoryId))
                .body("[0].category.name", notNullValue())

                .body("category.id", everyItem(equalTo(categoryId)));
    }
}
