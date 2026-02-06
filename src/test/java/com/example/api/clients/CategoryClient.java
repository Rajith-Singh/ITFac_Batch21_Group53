package com.example.api.clients;

import com.example.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CategoryClient {
    
    private static final String BASE_URL = ConfigReader.getProperty("base.url");
    private static final String CATEGORIES_ENDPOINT = "/api/categories";
    
    /**
     * Get all categories
     * @param token JWT authentication token
     * @return Response object containing categories list
     */
    public Response getAllCategories(String token) {
        return RestAssured
                .given()
                    .baseUri(BASE_URL)
                    .header("Authorization", "Bearer " + token)
                    .accept(ContentType.JSON)
                .when()
                    .get(CATEGORIES_ENDPOINT)
                .then()
                    .extract()
                    .response();
    }
    
    /**
     * Get category by ID
     * @param token JWT authentication token
     * @param categoryId Category ID
     * @return Response object containing category details
     */
    public Response getCategoryById(String token, Long categoryId) {
        return RestAssured
                .given()
                    .baseUri(BASE_URL)
                    .header("Authorization", "Bearer " + token)
                    .accept(ContentType.JSON)
                .when()
                    .get(CATEGORIES_ENDPOINT + "/" + categoryId)
                .then()
                    .extract()
                    .response();
    }
    
    /**
     * Create a new category
     * @param token JWT authentication token
     * @param categoryData Category data as request body
     * @return Response object containing created category
     */
    public Response createCategory(String token, Object categoryData) {
        return RestAssured
                .given()
                    .baseUri(BASE_URL)
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(categoryData)
                .when()
                    .post(CATEGORIES_ENDPOINT)
                .then()
                    .extract()
                    .response();
    }
    
    /**
     * Update an existing category
     * @param token JWT authentication token
     * @param categoryId Category ID to update
     * @param categoryData Updated category data
     * @return Response object containing updated category
     */
    public Response updateCategory(String token, Long categoryId, Object categoryData) {
        return RestAssured
                .given()
                    .baseUri(BASE_URL)
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(categoryData)
                .when()
                    .put(CATEGORIES_ENDPOINT + "/" + categoryId)
                .then()
                    .extract()
                    .response();
    }
    
    /**
     * Delete a category
     * @param token JWT authentication token
     * @param categoryId Category ID to delete
     * @return Response object
     */
    public Response deleteCategory(String token, Long categoryId) {
        return RestAssured
                .given()
                    .baseUri(BASE_URL)
                    .header("Authorization", "Bearer " + token)
                    .accept(ContentType.JSON)
                .when()
                    .delete(CATEGORIES_ENDPOINT + "/" + categoryId)
                .then()
                    .extract()
                    .response();
    }
    
    /**
     * Get main categories only (categories without parent)
     * @param token JWT authentication token
     * @return Response object containing main categories
     */
    public Response getMainCategories(String token) {
        return RestAssured
                .given()
                    .baseUri(BASE_URL)
                    .header("Authorization", "Bearer " + token)
                    .accept(ContentType.JSON)
                .when()
                    .get(CATEGORIES_ENDPOINT + "/main")
                .then()
                    .extract()
                    .response();
    }
    
    /**
     * Get sub-categories only (categories with parent)
     * @param token JWT authentication token
     * @return Response object containing sub-categories
     */
    public Response getSubCategories(String token) {
        return RestAssured
                .given()
                    .baseUri(BASE_URL)
                    .header("Authorization", "Bearer " + token)
                    .accept(ContentType.JSON)
                .when()
                    .get(CATEGORIES_ENDPOINT + "/sub-categories")
                .then()
                    .extract()
                    .response();
    }
}
