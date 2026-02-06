#!/bin/bash

echo "Running All Sales Tests TC_UI_SAL_11 to TC_UI_SAL_17 (Using Updated Main TestNG)..."
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

# Compile project
echo "Compiling project..."
mvn clean compile test-compile

# Run tests using main testng.xml (now updated to include TC_UI_SAL_17)
echo "Running All Sales Tests TC_UI_SAL_11 to TC_UI_SAL_17..."
mvn test

# Check test results
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ All Sales Tests TC_UI_SAL_11 to TC_UI_SAL_17 executed successfully!"
    echo "Test Results: target/surefire-reports/"
    echo ""
    echo "Total tests should be: 7"
    echo "Check test summary to verify TC_UI_SAL_17 was included"
else
    echo ""
    echo "❌ Some Sales Tests failed!"
    echo "Check test results in: target/surefire-reports/"
fi