package com.example.api.clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CategoryClient {
    private static final String BASE_URL = "http://localhost:8081/api/categories";
    
    public Response getCategories(String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(BASE_URL);
    }
    
    public Response getCategoryById(String token, int categoryId) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(BASE_URL + "/" + categoryId);
    }
    
    public Response createCategory(String token, Map<String, Object> categoryRequest) {
        return given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(categoryRequest)
                .when()
                .post(BASE_URL);
    }
    
    public Response updateCategory(String token, int categoryId, Map<String, Object> updateRequest) {
        return given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(updateRequest)
                .when()
                .put(BASE_URL + "/" + categoryId);
    }
    
    public Response deleteCategory(String token, int categoryId) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(BASE_URL + "/" + categoryId);
    }
    
    public Response searchCategories(String token, String searchTerm) {
        return given()
                .header("Authorization", "Bearer " + token)
                .queryParam("name", searchTerm)
                .when()
                .get(BASE_URL + "/search");
    }
    
    public Response getCategoriesPaginated(String token, int page, int size) {
        return given()
                .header("Authorization", "Bearer " + token)
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(BASE_URL + "/paged");
    }
}
