package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features/ui/sales", glue = {
        "com.example.ui.stepDefinitions",
        "com.example.hooks"
}, plugin = {
        "pretty",
        "html:target/cucumber-reports/sales-sorting/cucumber.html",
        "json:target/cucumber-reports/sales-sorting/cucumber.json"
}, monochrome = true, tags = "@TC_UI_SAL_11 or @TC_UI_SAL_12 or @TC_UI_SAL_13")
public class SalesSortingTestRunner extends AbstractTestNGCucumberTests {
}