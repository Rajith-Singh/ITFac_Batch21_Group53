package com.example.api.context;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Shared context for API Cucumber steps. Holds auth token and last response
 * so Auth steps and API steps can share state within a scenario.
 */
public class ApiTestContext {
    private String token;
    private String role;
    private Response lastResponse;
    private Response ascendingSortResponse;
    private Response descendingSortResponse;
    private int lastTotalElements = -1;
    private final Map<String, Response> responsesByKey = new HashMap<>();

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }

    public int getLastTotalElements() {
        return lastTotalElements;
    }

    public void setLastTotalElements(int lastTotalElements) {
        this.lastTotalElements = lastTotalElements;
    }

    public Response getAscendingSortResponse() {
        return ascendingSortResponse;
    }

    public void setAscendingSortResponse(Response ascendingSortResponse) {
        this.ascendingSortResponse = ascendingSortResponse;
    }

    public Response getDescendingSortResponse() {
        return descendingSortResponse;
    }

    public void setDescendingSortResponse(Response descendingSortResponse) {
        this.descendingSortResponse = descendingSortResponse;
    }

    public void setResponseByKey(String key, Response response) {
        this.responsesByKey.put(key, response);
    }

    public Response getResponseByKey(String key) {
        return this.responsesByKey.get(key);
    }

    public void clear() {
        token = null;
        role = null;
        lastResponse = null;
        ascendingSortResponse = null;
        descendingSortResponse = null;
        lastTotalElements = -1;
        responsesByKey.clear();
    }
}
