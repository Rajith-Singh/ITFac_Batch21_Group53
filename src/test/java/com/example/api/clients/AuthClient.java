package com.example.api.clients;

import com.example.api.models.request.LoginRequest;
import com.example.api.models.response.LoginResponse;
import com.example.utils.ConfigReader;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {
    private static final String BASE_URL = "http://localhost:8081";
    
    public String loginUser(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        
        Response response = given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(BASE_URL + "/api/auth/login");
        
        if (response.getStatusCode() == 200) {
            LoginResponse loginResponse = response.as(LoginResponse.class);
            return loginResponse.getToken();
        }
        return null;
    }
    
    public String getUserToken() {
        String username = ConfigReader.getProperty("user.username");
        String password = ConfigReader.getProperty("user.password");
        return loginUser(username, password);
    }
    
    public String getAdminToken() {
        String username = ConfigReader.getProperty("admin.username");
        String password = ConfigReader.getProperty("admin.password");
        return loginUser(username, password);
    }
}