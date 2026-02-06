package com.example.api.stepDefinitions;

import com.example.api.clients.AuthClient;
import com.example.api.models.response.LoginResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.testng.Assert;

public class AuthApiSteps {
    
    private AuthClient authClient;
    private Response response;
    private String authToken;
    
    public AuthApiSteps() {
        this.authClient = new AuthClient();
    }
    
    @Given("I have valid admin credentials")
    public void i_have_valid_admin_credentials() {
        // Credentials are loaded from config.properties
        // This step just validates they are available
        Assert.assertNotNull(authClient, "AuthClient should be initialized");
    }
    
    @Given("I have valid user credentials")
    public void i_have_valid_user_credentials() {
        // Credentials are loaded from config.properties
        // This step just validates they are available
        Assert.assertNotNull(authClient, "AuthClient should be initialized");
    }
    
    @When("I authenticate as admin")
    public void i_authenticate_as_admin() {
        authToken = authClient.getAdminToken();
        Assert.assertNotNull(authToken, "Admin token should not be null");
    }
    
    @When("I authenticate as user")
    public void i_authenticate_as_user() {
        authToken = authClient.getUserToken();
        Assert.assertNotNull(authToken, "User token should not be null");
    }
    
    @When("I send login request with username {string} and password {string}")
    public void i_send_login_request(String username, String password) {
        response = authClient.login(username, password);
    }
    
    @Then("I should receive a valid authentication token")
    public void i_should_receive_valid_token() {
        Assert.assertNotNull(authToken, "Authentication token should not be null");
        Assert.assertTrue(authToken.length() > 0, "Authentication token should not be empty");
    }
    
    @Then("the login response should have status code {int}")
    public void the_login_response_status_code(int statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode, 
            "Status code should be " + statusCode);
    }
    
    @Then("the login response should contain a token")
    public void the_login_response_contains_token() {
        LoginResponse loginResponse = response.as(LoginResponse.class);
        Assert.assertNotNull(loginResponse.getToken(), "Token should not be null");
        Assert.assertTrue(loginResponse.getToken().length() > 0, "Token should not be empty");
    }
    
    // Getter for sharing authentication token with other step definitions
    public String getAuthToken() {
        return authToken;
    }
    
    public void setAuthToken(String token) {
        this.authToken = token;
    }
}
