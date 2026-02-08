package com.example.api.base;

import com.example.api.config.ApiConfig;
import com.example.api.constants.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeSuite;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;
    protected static String userToken;

    @BeforeSuite(alwaysRun = true)
    public void setup() {

        RestAssured.baseURI = ApiConfig.getBaseUrl();

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        // ===== AUTO LOGIN =====
        Response loginResponse =
                given()
                        .contentType(ContentType.JSON)
                        .body("{\"username\":\"admin\",\"password\":\"admin123\"}")
                        .post(ApiEndpoints.AUTH_LOGIN);

        userToken = loginResponse.jsonPath().getString("token");

        System.out.println("JWT TOKEN => " + userToken);
    }
}
