package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features/ui/sales/sales_list_sold_date_sort_indicator.feature",
    glue = {"com.example.ui.stepDefinitions.sales", "com.example.utils"},
    plugin = {"pretty", "html:target/cucumber-reports/SalesListSoldDateSortIndicatorTestRunner.html"},
    monochrome = true
)
public class SalesListSoldDateSortIndicatorTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)  // This forces sequential execution
    public Object[][] scenarios() {
        return super.scenarios();
    }
}