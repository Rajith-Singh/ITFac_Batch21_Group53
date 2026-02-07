package com.example.runners;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features/ui/sales", glue = {
        "com.example.ui.stepDefinitions",
        "com.example.hooks"
}, plugin = {
        "pretty",
        "html:target/cucumber-reports/sales-sorting/cucumber.html",
        "json:target/cucumber-reports/sales-sorting/cucumber.json"
}, monochrome = true)
public class SalesSortingTestRunner extends AbstractTestNGCucumberTests {
            @Override
    @DataProvider(parallel = false)  // This forces sequential execution
    public Object[][] scenarios() {
        return super.scenarios();
    }
}