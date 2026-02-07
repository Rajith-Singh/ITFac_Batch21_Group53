package com.example.api.clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class PlantClient {
    private static final String BASE_URL = "http://localhost:8081/api/plants";
    
    public Response getPagedPlants(String token, int page, int size) {
        return given()
                .header("Authorization", "Bearer " + token)
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(BASE_URL + "/paged");
    }
    
    public Response getPagedPlantsWithSort(String token, String sortBy, String sortOrder) {
        return given()
                .header("Authorization", "Bearer " + token)
                .queryParam("sort", sortBy + "," + sortOrder)
                .when()
                .get(BASE_URL + "/paged");
    }
    
    public Response searchPlants(String token, String searchTerm) {
        return given()
                .header("Authorization", "Bearer " + token)
                .queryParam("name", searchTerm)
                .when()
                .get(BASE_URL + "/paged");
    }
    
    public Response createPlant(String token, String name, double price, int quantity, int categoryId) {
        String requestBody = String.format(
            "{\"name\": \"%s\", \"price\": %.2f, \"quantity\": %d}",
            name, price, quantity);
        
        return given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + "/category/" + categoryId);
    }
    
    public Response filterPlantsByCategory(String token, String category) {
        return given()
                .header("Authorization", "Bearer " + token)
                .queryParam("category", category)
                .when()
                .get(BASE_URL + "/paged");
    }
    
    public Response getPagedPlants(String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(BASE_URL + "/paged");
    }
    
    public Response createPlantWithoutName(String token, double price, int quantity, int categoryId) {
        String requestBody = String.format(
            "{\"price\": %.2f, \"quantity\": %d}",
            price, quantity);
        
        return given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + "/category/" + categoryId);
    }
    
    public Response createPlantWithoutPrice(String token, String name, int quantity, int categoryId) {
        String requestBody = String.format(
            "{\"name\": \"%s\", \"quantity\": %d}",
            name, quantity);
        
        return given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + "/category/" + categoryId);
    }
    
    public Response createPlantWithoutStock(String token, String name, double price, int categoryId) {
        String requestBody = String.format(
            "{\"name\": \"%s\", \"price\": %.2f}",
            name, price);
        
        return given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + "/category/" + categoryId);
    }
    
    public Response createPlantWithEmptyName(String token, double price, int quantity, int categoryId) {
        String requestBody = String.format(
            "{\"name\": \"\", \"price\": %.2f, \"quantity\": %d}",
            price, quantity);
        
        return given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + "/category/" + categoryId);
    }
}
