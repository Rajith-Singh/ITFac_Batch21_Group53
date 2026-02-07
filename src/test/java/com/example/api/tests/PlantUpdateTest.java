package com.example.api.tests;

import com.example.api.base.BaseTest;
import com.example.api.constants.ApiEndpoints;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class PlantUpdateTest extends BaseTest {

    @Test
    public void TC_API_PLANTS_ADMIN_020_verifyPlantUpdatedSuccessfully() {

        String requestBody =
                "{\n" +
                "  \"id\": 0,\n" +
                "  \"name\": \"Anthurium\",\n" +
                "  \"price\": 150,\n" +
                "  \"quantity\": 25,\n" +
                "  \"category\": {\n" +
                "    \"id\": 0,\n" +
                "    \"name\": \"Anthurium\",\n" +
                "    \"parent\": \"string\",\n" +
                "    \"subCategories\": [\"string\"]\n" +
                "  }\n" +
                "}";

        RestAssured.given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + userToken)
                .body(requestBody)

                .when()
                .put(ApiEndpoints.UPDATE_PLANT)

                .then()
                .spec(responseSpec)
                .statusCode(200)

                // Plant fields
                .body("id", equalTo(0))
                .body("name", equalTo("Anthurium"))
                .body("price", equalTo(150))
                .body("quantity", equalTo(25))

                // Category fields
                .body("category.id", equalTo(0))
                .body("category.name", equalTo("Anthurium"))
                .body("category.parent", equalTo("string"))
                .body("category.subCategories[0]", equalTo("string"));
    }
}
