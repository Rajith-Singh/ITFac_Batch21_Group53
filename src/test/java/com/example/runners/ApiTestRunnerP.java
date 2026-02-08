// src/test/java/com/example/runners/ApiTestRunner.java
package com.example.runners;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features/api/plantsP", glue = {
        "com.example.api.stepDefinitions",
        "com.example.api.context",
        "com.example.api.hooks",
        "com.example.hooks"
}, plugin = {
        "pretty",
        "html:target/cucumber-reports/api/cucumber.html",
        "json:target/cucumber-reports/api/cucumber.json"
}, monochrome = true)
public class ApiTestRunnerP extends AbstractTestNGCucumberTests {
            @Override
    @DataProvider(parallel = false)  // This forces sequential execution
    public Object[][] scenarios() {
        return super.scenarios();
    }
}