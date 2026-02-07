#!/usr/bin/env bash
# Run API tests only (Cucumber feature files under src/test/resources/features/api/)
# Requires backend at http://localhost:8081 for full pass. Category filter test may fail due to known API bug.
set -e
cd "$(dirname "$0")"
mvn test -Dtest=ApiTestRunner -q
