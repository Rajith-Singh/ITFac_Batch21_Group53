package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features/ui", glue = {
        "com.example.ui.stepDefinitions",
        "com.example.hooks"
}, plugin = {
        "pretty",
        "html:target/cucumber-reports/ui/plants-cucumber.html",
        "json:target/cucumber-reports/ui/plants-cucumber.json"
}, monochrome = true, tags = "@plants")
public class PlantTestRunner extends AbstractTestNGCucumberTests {
}
