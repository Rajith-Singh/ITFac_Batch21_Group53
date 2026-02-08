package com.example.api.utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ApiUtils {

    public static JsonPath getJsonPath(Response response) {
        return response.jsonPath();
    }

    public static String getFieldValue(Response response, String field) {
        return response.jsonPath().get(field).toString();
    }
}
