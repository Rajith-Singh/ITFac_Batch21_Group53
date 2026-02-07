package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.example",
    plugin = {"pretty", "html:target/cucumber-reports/cucumber.html"}
)
public class CucumberRunner extends AbstractTestNGCucumberTests {
}