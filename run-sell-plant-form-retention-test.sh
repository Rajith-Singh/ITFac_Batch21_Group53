#!/bin/bash

echo "Running Sell Plant Form Retention Test (TC_UI_SAL_17)..."
echo "Test: Verify form retains data after validation error"
echo ""

# Clean previous test results
echo "Cleaning previous test results..."
rm -rf target/surefire-reports/
rm -rf target/test-classes/

# Compile the project
echo "Compiling the project..."
mvn clean compile test-compile

# Run the specific test
echo "Running Sell Plant Form Retention Test..."
mvn test -DsuiteXmlFile=testng-sell-plant-form-retention.xml

# Check test results
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ TC_UI_SAL_17 test executed successfully!"
    echo "Test Results: target/surefire-reports/"
else
    echo ""
    echo "❌ TC_UI_SAL_17 test failed!"
    echo "Check test results in: target/surefire-reports/"
fi