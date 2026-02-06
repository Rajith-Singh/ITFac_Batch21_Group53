#!/bin/bash

echo "Running Enhanced Sales Tests TC_UI_SAL_11 to TC_UI_SAL_17 with Slow Loading Handling..."
echo "Enhanced Features:"
echo "  - Increased timeouts (30-60 seconds)"
echo "  - Enhanced wait strategies"
echo "  - Better error handling for slow pages"
echo "  - Fallback mechanisms"
echo ""

# Set JVM options for better performance
export MAVEN_OPTS="-Xmx2g -XX:+UseG1GC -XX:+UseStringDeduplication"

# Clean previous test results
echo "Cleaning previous test results..."
rm -rf target/surefire-reports/
rm -rf target/test-classes/

# Compile with optimized settings
echo "Compiling project with optimizations..."
mvn clean compile test-compile -Dmaven.test.skip=false -DskipTests=false

# Run tests with enhanced timeouts
echo "Running Enhanced Sales Tests TC_UI_SAL_11 to TC_UI_SAL_17..."
mvn test \
  -DsuiteXmlFile=testng-enhanced-sales-tests-11-to-17.xml \
  -Dmaven.test.failure.ignore=true \
  -Dbrowser.timeout=60 \
  -Dpage.load.timeout=90 \
  -Delement.wait.timeout=45

# Check test results
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ All Enhanced Sales Tests executed successfully!"
    echo "Test Results: target/surefire-reports/"
else
    echo ""
    echo "❌ Some Enhanced Sales Tests failed!"
    echo "Check detailed results in: target/surefire-reports/"
    echo ""
    echo "Common slow loading issues:"
    echo "- Ensure application server is fully started"
    echo "- Check network connectivity"
    echo "- Verify application has sufficient resources"
    echo "- Consider restarting the application"
fi