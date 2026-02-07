package com.example.api.stepDefinitions;

import com.example.api.clients.AuthClient;
import com.example.api.context.ApiTestContext;
import io.cucumber.java.en.Given;
import org.testng.Assert;

public class AuthApiSteps {

    private final ApiTestContext context;
    private final AuthClient authClient = new AuthClient();

    public AuthApiSteps(ApiTestContext context) {
        this.context = context;
    }

    @Given("user is authenticated")
    public void user_is_authenticated() {
        String token = authClient.getUserToken();
        Assert.assertNotNull(token, "User token should not be null");
        context.setToken(token);
        context.setRole("user");
    }

    @Given("admin is authenticated")
    public void admin_is_authenticated() {
        String token = authClient.getAdminToken();
        context.setToken(token);
        context.setRole("admin");
    }
}