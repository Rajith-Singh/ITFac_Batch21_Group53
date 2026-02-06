#!/bin/bash

echo "Running TC_UI_SALES_USER_08 - Total Price Sorting for Regular User Test"
echo "======================================================================="

# Clean previous test results
echo "Cleaning previous test results..."
rm -rf target/cucumber-reports/*total-price-sorting-user*
rm -rf target/surefire-reports/*total-price-sorting-user*

# Run the test
echo "Executing the test..."
mvn test -Dtest=SalesListTotalPriceSortingUserTestRunner

# Check results
if [ $? -eq 0 ]; then
    echo "✅ Test completed successfully!"
    echo "📄 Report available at: target/cucumber-reports/SalesListTotalPriceSortingUserTestRunner.html"
else
    echo "❌ Test failed. Check the logs for details."
    exit 1
fi