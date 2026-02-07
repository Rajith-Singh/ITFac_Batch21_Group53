package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features",  glue = {
        "com.example.ui.stepDefinitions.dashboard", // For login steps
        "com.example.ui.stepDefinitions.sales", // For sales steps
        "com.example.hooks"
}, plugin = {
        "pretty",
        "html:target/cucumber-reports/sales/cucumber.html",
        "json:target/cucumber-reports/sales/cucumber.json"
}, monochrome = true, tags = "@Sales")
public class SalesTestRunner extends AbstractTestNGCucumberTests {
}