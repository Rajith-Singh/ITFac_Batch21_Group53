// src/test/java/com/example/runners/ApiTestRunner.java
package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features/api", glue = {
        "com.example.api.stepDefinitions",
        "com.example.hooks"
}, plugin = {
        "pretty",
        "html:target/cucumber-reports/api/cucumber.html",
        "json:target/cucumber-reports/api/cucumber.json"
}, monochrome = true, tags = "@api and not @Manual")
public class ApiTestRunner extends AbstractTestNGCucumberTests {
}