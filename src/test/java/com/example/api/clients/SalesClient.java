package com.example.api.clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SalesClient {
    private static final String BASE_URL = "http://localhost:8081/api/sales";
    
    public Response createSale(String token, Map<String, Object> saleRequest) {
        return given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(saleRequest)
                .when()
                .post(BASE_URL);
    }
    
    public Response getSales(String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(BASE_URL);
    }
    
    public Response getSalesPaginated(String token, int page, int size) {
        return given()
                .header("Authorization", "Bearer " + token)
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(BASE_URL + "/paged");
    }
    
    public Response getSalesSorted(String token, String sortBy, String sortOrder) {
        return given()
                .header("Authorization", "Bearer " + token)
                .queryParam("sort", sortBy + "," + sortOrder)
                .when()
                .get(BASE_URL + "/paged");
    }
    
    public Response getSalesByDateRange(String token, String startDate, String endDate) {
        return given()
                .header("Authorization", "Bearer " + token)
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .when()
                .get(BASE_URL + "/filter");
    }
    
    public Response getSalesByPlant(String token, String plantName) {
        return given()
                .header("Authorization", "Bearer " + token)
                .queryParam("plant", plantName)
                .when()
                .get(BASE_URL + "/filter");
    }
    
    public Response getAllSales(String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(BASE_URL + "/admin/all");
    }
}
