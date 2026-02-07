package com.example.api.clients;

import com.example.api.models.response.LoginResponse;
import com.example.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class Auth_Client {
    
    private static final String BASE_URL = ConfigReader.getProperty("base.url");
    private static final String LOGIN_ENDPOINT = "/api/auth/login";
    
    /**
     * Authenticate user and get JWT token
     * @param username Username for authentication
     * @param password Password for authentication
     * @return Response object containing authentication response
     */
    public Response login(String username, String password) {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", username);
        loginRequest.put("password", password);
        
        return RestAssured
                .given()
                    .baseUri(BASE_URL)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(loginRequest)
                .when()
                    .post(LOGIN_ENDPOINT)
                .then()
                    .extract()
                    .response();
    }
    
    /**
     * Get JWT token for admin user
     * @return JWT token string
     */
    public String getAdminToken() {
        String username = ConfigReader.getProperty("admin.username");
        String password = ConfigReader.getProperty("admin.password");
        
        Response response = login(username, password);
        LoginResponse loginResponse = response.as(LoginResponse.class);
        return loginResponse.getToken();
    }
    
    /**
     * Get JWT token for regular user
     * @return JWT token string
     */
    public String getUserToken() {
        String username = ConfigReader.getProperty("user.username");
        String password = ConfigReader.getProperty("user.password");
        
        Response response = login(username, password);
        LoginResponse loginResponse = response.as(LoginResponse.class);
        return loginResponse.getToken();
    }
}