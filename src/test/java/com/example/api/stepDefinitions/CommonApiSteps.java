package com.example.api.stepDefinitions;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.testng.Assert;

/**
 * Shared API step definitions to avoid DuplicateStepDefinitionException.
 * CategoryApiSteps and SalesAdminApiStepDefinitions must call setLastResponse()
 * whenever they store an API response.
 */
public class CommonApiSteps {

    private static Response lastResponse;

    public static void setLastResponse(Response response) {
        lastResponse = response;
    }

    public static Response getLastResponse() {
        return lastResponse;
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {
        if (lastResponse == null) {
            Assert.fail("Response is null - request might have failed");
        }
        int actualStatusCode = lastResponse.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode,
                "Response status code should be " + expectedStatusCode + " but was " + actualStatusCode);
    }

    @Then("the response status code should be 200 or 204")
    public void the_response_status_code_should_be_200_or_204() {
        if (lastResponse == null) {
            Assert.fail("Response is null - request might have failed");
        }
        int actualStatusCode = lastResponse.getStatusCode();
        boolean isValid = actualStatusCode == 200 || actualStatusCode == 204;
        Assert.assertTrue(isValid,
                "Response status code should be 200 or 204 but was " + actualStatusCode);
    }
}
