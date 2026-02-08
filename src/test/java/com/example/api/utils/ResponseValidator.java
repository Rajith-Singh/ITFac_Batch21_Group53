package com.example.api.utils;

import io.restassured.response.Response;
import org.testng.Assert;

public class ResponseValidator {

    public static void assertStatusCode(Response response, int statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode, "Status code mismatch!");
    }

    public static void assertMessage(Response response, String expectedMessage) {
        String actualMessage = response.jsonPath().get("message");
        Assert.assertEquals(actualMessage, expectedMessage, "Message mismatch!");
    }
}
