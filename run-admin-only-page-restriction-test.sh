#!/bin/bash

echo "Running TC_UI_PLANTS_USER_10 - Admin-only Page Restriction Test"
echo "==============================================================="

# Clean previous test results
echo "Cleaning previous test results..."
rm -rf target/cucumber-reports/*admin-only-page-restriction*
rm -rf target/surefire-reports/*admin-only-page-restriction*

# Run the test
echo "Executing the test..."
mvn test -Dtest=AdminOnlyPageRestrictionTestRunner

# Check results
if [ $? -eq 0 ]; then
    echo "✅ Test completed successfully!"
    echo "📄 Report available at: target/cucumber-reports/AdminOnlyPageRestrictionTestRunner.html"
else
    echo "❌ Test failed. Check the logs for details."
    exit 1
fi