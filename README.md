# ITFac_Batch21_Group53

## API tests (Cucumber)

API tests are written as **Cucumber feature files** under `src/test/resources/features/api/` and run with the same stack as UI tests (TestNG + Cucumber).

- **Feature file:** `src/test/resources/features/api/plants.feature`
- **Step definitions:** `com.example.api.stepDefinitions` (AuthApiSteps, PlantApiSteps)
- **Runner:** `com.example.runners.ApiTestRunner`

**Run API tests only:**

```bash
./run-api-tests.sh
# or
mvn test -Dtest=ApiTestRunner
```

**Run API tests via TestNG suite (API-only suite):**

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-api.xml
```

**Run all tests (including API):** the main `testng.xml` includes an "API Tests" test; use `mvn test` to run the full suite.

**Note:** Backend must be available at `http://localhost:8081` for auth and plant API calls. The category filter scenario (TC_API_PLANTS_USER_12) may fail if the API does not filter by category (known application bug).