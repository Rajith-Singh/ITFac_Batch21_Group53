package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features/ui/plants/admin_only_page_restriction.feature",
    glue = {"com.example.ui.stepDefinitions.plants", "com.example.ui.stepDefinitions.sales", "com.example.utils"},
    plugin = {"pretty", "html:target/cucumber-reports/AdminOnlyPageRestrictionTestRunner.html"},
    monochrome = true
)
public class AdminOnlyPageRestrictionTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}