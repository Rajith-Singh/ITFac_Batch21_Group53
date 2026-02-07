// src/test/java/com/example/runners/DUiTestRunner.java
package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features/ui/dashboard", glue = {
        "com.example.ui.stepDefinitions",
        "com.example.hooks"
}, plugin = {
        "pretty",
        "html:target/cucumber-reports/ui/cucumber.html",
        "json:target/cucumber-reports/ui/cucumber.json"
}, monochrome = true, tags = "@ui and @dashboard")
public class DUiTestRunner extends AbstractTestNGCucumberTests {
}