#!/bin/bash

echo "Running TC_UI_SALES_USER_07 - Sold Date Sort Indicator Test"
echo "============================================================"

# Clean previous test results
echo "Cleaning previous test results..."
rm -rf target/cucumber-reports/*sales-sold-date-sort-indicator*
rm -rf target/surefire-reports/*sales-sold-date-sort-indicator*

# Run the test
echo "Executing the test..."
mvn test -Dtestng.xml=testng-sales-sold-date-sort-indicator.xml

# Check results
if [ $? -eq 0 ]; then
    echo "✅ Test completed successfully!"
    echo "📄 Report available at: target/cucumber-reports/SalesListSoldDateSortIndicatorTestRunner.html"
else
    echo "❌ Test failed. Check the logs for details."
    exit 1
fi