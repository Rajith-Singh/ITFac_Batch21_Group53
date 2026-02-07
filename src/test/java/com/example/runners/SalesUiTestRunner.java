// src/test/java/com/example/runners/SalesUiTestRunner.java
package com.example.runners;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features/ui", glue = {
        "com.example.ui.stepDefinitions",
        "com.example.hooks"
}, plugin = {
        "pretty",
        "html:target/cucumber-reports/ui/cucumber.html",
        "json:target/cucumber-reports/ui/cucumber.json"
}, monochrome = true)
public class SalesUiTestRunner extends AbstractTestNGCucumberTests {
            @Override
    @DataProvider(parallel = false)  // This forces sequential execution
    public Object[][] scenarios() {
        return super.scenarios();
    }
}