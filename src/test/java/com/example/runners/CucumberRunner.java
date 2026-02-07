package com.example.runners;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.example",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber.html",
        "json:target/cucumber-reports/cucumber.json" // Added for JSON reporting
    },
    monochrome = true // Added for cleaner console output
)
public class CucumberRunner extends AbstractTestNGCucumberTests {
        @Override
    @DataProvider(parallel = false)  // This forces sequential execution
    public Object[][] scenarios() {
        return super.scenarios();
    }
}