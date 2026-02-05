#!/bin/bash

echo "=== Running Sales Sorting Tests in Order ==="
echo ""

echo "1. Running TC_UI_SAL_11 - Plant Name Sorting..."
mvn test -Dcucumber.options="@src/test/resources/features/ui/sales/sales_list_sorting.feature" -Dcucumber.filter.tags="@TC_UI_SAL_11" -q
if [ $? -eq 0 ]; then
    echo "✅ TC_UI_SAL_11 PASSED"
else
    echo "❌ TC_UI_SAL_11 FAILED"
fi
echo ""

echo "2. Running TC_UI_SAL_12 - Quantity Sorting..."
mvn test -Dcucumber.options="@src/test/resources/features/ui/sales/sales_list_quantity_sorting.feature" -Dcucumber.filter.tags="@TC_UI_SAL_12" -q
if [ $? -eq 0 ]; then
    echo "✅ TC_UI_SAL_12 PASSED"
else
    echo "❌ TC_UI_SAL_12 FAILED"
fi
echo ""

echo "3. Running TC_UI_SAL_13 - Total Price Sorting..."
mvn test -Dcucumber.options="@src/test/resources/features/ui/sales/sales_list_total_price_sorting.feature" -Dcucumber.filter.tags="@TC_UI_SAL_13" -q
if [ $? -eq 0 ]; then
    echo "✅ TC_UI_SAL_13 PASSED"
else
    echo "❌ TC_UI_SAL_13 FAILED"
fi
echo ""

echo "=== Test Execution Complete ==="