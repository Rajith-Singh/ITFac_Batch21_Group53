#!/bin/bash

echo "Running Plant API Tests..."
echo "Test Cases: TC_API_PLANTS_USER_06, TC_API_PLANTS_USER_07, TC_API_PLANTS_USER_08, TC_API_PLANTS_USER_09, TC_API_PLANTS_USER_10, TC_API_PLANTS_USER_11, TC_API_PLANTS_USER_12, TC_API_PLANTS_ADM_01"
echo "=========================================="

# Run the plant API tests using Maven
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-plant-pagination-tests.xml

# Check the exit status
if [ $? -eq 0 ]; then
    echo "=========================================="
    echo "✅ Plant API Tests completed successfully!"
    echo "Results available in target/surefire-reports/"
else
    echo "=========================================="
    echo "❌ Plant API Tests failed!"
    echo "Check target/surefire-reports/ for detailed results"
    exit 1
fi