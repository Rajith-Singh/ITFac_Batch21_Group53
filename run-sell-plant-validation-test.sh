#!/bin/bash

echo "Running TC_UI_SAL_15 - Sell Plant Validation Test"
echo "================================================"

# Clean and compile the project
echo "Cleaning and compiling..."
mvn clean compile test-compile

# Run the specific test
echo "Running TC_UI_SAL_15 test..."
mvn test -Dsurefire.suiteXmlFiles="src/test/resources/testng-sell-plant-validation.xml"

echo "Test execution completed."