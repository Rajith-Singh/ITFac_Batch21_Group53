#!/bin/bash

echo "Running Sales Tests TC_UI_SAL_11 to TC_UI_SAL_17 respectively..."
echo "Test Cases:"
echo "  - TC_UI_SAL_11: Sales List Sorting"
echo "  - TC_UI_SAL_12: Sales List Quantity Sorting" 
echo "  - TC_UI_SAL_13: Sales List Total Price Sorting"
echo "  - TC_UI_SAL_14: Sell Plant Button Navigation"
echo "  - TC_UI_SAL_15: Error when Plant not selected"
echo "  - TC_UI_SAL_16: Dropdown updates after sales (multi-tab)"
echo "  - TC_UI_SAL_17: Form retains data after validation error"
echo ""

# Clean previous test results
echo "Cleaning previous test results..."
rm -rf target/surefire-reports/
rm -rf target/test-classes/

# Compile the project
echo "Compiling the project..."
mvn clean compile test-compile

# Run the tests
echo "Running Sales Tests TC_UI_SAL_11 to TC_UI_SAL_17..."
mvn test -Dsurefire.suiteXmlFiles=testng-sales-tests-11-to-17.xml

# Check test results
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ All Sales Tests TC_UI_SAL_11 to TC_UI_SAL_17 executed successfully!"
    echo "Test Results: target/surefire-reports/"
else
    echo ""
    echo "❌ Some Sales Tests failed!"
    echo "Check test results in: target/surefire-reports/"
fi