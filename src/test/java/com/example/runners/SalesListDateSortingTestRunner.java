package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features/ui/sales/sales_list_date_sorting.feature",
    glue = {"com.example.ui.stepDefinitions.sales", "com.example.utils"},
    plugin = {"pretty", "html:target/cucumber-reports/SalesListDateSortingTestRunner.html"},
    monochrome = true
)
public class SalesListDateSortingTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}