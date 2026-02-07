package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
    features = {"src/test/resources/features/ui/sales/sales_list_sorting.feature"},
    glue = {
        "com.example.ui.stepDefinitions.sales",
        "com.example.ui.stepDefinitions.login",
        "com.example.hooks"
    },
    plugin = {
        "pretty",
        "html:target/cucumber-reports/sales-sorting-report.html",
        "json:target/cucumber-reports/sales-sorting-report.json"
    },
    monochrome = true,
    dryRun = false
)
public class SalesListSortingTestRunner extends AbstractTestNGCucumberTests {
        @Override
    @DataProvider(parallel = false)  // This forces sequential execution
    public Object[][] scenarios() {
        return super.scenarios();
    }
}