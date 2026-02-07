package com.example.api.hooks;

import com.example.api.context.ApiTestContext;
import io.cucumber.java.Before;

/**
 * Cucumber hooks for API scenarios. Clears shared context before each API scenario.
 */
public class ApiHooks {

    private final ApiTestContext context;

    public ApiHooks(ApiTestContext context) {
        this.context = context;
    }

    @Before("@api")
    public void clearContext() {
        context.clear();
    }
}
