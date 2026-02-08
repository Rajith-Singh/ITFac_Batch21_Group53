package com.example.api.clients;

import com.example.api.constants.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class Auth_PClient {

    public Response login(String username, String password) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        return RestAssured.given()
                .body(credentials)
                .post(ApiEndpoints.AUTH_LOGIN);
    }
}