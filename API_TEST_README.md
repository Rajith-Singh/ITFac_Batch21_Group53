# Category API Test - TC_API_CAT_USER_01

## Overview
This document describes the implementation of test case **TC_API_CAT_USER_01** - "Verify User can retrieve all categories via API"

## Test Case Details

### TC_API_CAT_USER_01
- **ID**: TC_API_CAT_USER_01
- **Title**: Verify User can retrieve all categories via API  
- **Description**: Test basic categories listing endpoint with User credentials
- **Type**: API Test using Cucumber BDD + Rest Assured

### Preconditions
- Regular User authentication token available
- Multiple categories exist in system (≥5 main categories, some with sub-categories)
- User has no Admin privileges

### Test Steps
1. Authenticate as regular user
2. Send GET request to `/api/categories`
3. Set Authorization header: `Bearer {user_token}`
4. Set Accept header: `application/json`
5. Capture response status and body
6. Verify response structure
7. Count categories returned

### Expected Results
- ✅ Returns HTTP 200 OK
- ✅ Response is JSON array of category objects
- ✅ Each category contains: `id`, `name`, `parentCategory`, `isMainCategory` flag
- ✅ Returns all categories (both main and sub-categories)
- ✅ No Admin-only fields present

## Project Structure

```
src/test/java/com/example/api/
├── clients/
│   ├── AuthClient.java          # REST client for authentication
│   └── CategoryClient.java      # REST client for category operations
├── models/
│   └── response/
│       ├── LoginResponse.java   # Login API response model
│       └── CategoryResponse.java # Category API response model
└── stepDefinitions/
    ├── AuthApiSteps.java        # Cucumber steps for authentication
    └── CategoryApiSteps.java    # Cucumber steps for category tests

src/test/resources/features/api/
└── categories_api.feature       # Cucumber feature file for category API tests
```

## Implementation Details

### 1. Model Classes

#### CategoryResponse.java
Represents the category object returned by the API:
```java
- id (Long)
- name (String)
- parentCategory (String)
- isMainCategory (Boolean)
```

#### LoginResponse.java
Represents the authentication response:
```java
- token (String)
- type (String)
- username (String)
- roles (String[])
```

### 2. Client Classes

#### AuthClient.java
Handles authentication operations:
- `login(username, password)` - Authenticate user
- `getAdminToken()` - Get admin JWT token
- `getUserToken()` - Get regular user JWT token

#### CategoryClient.java
Handles category API operations:
- `getAllCategories(token)` - GET /api/categories
- `getCategoryById(token, id)` - GET /api/categories/{id}
- `createCategory(token, data)` - POST /api/categories
- `updateCategory(token, id, data)` - PUT /api/categories/{id}
- `deleteCategory(token, id)` - DELETE /api/categories/{id}

### 3. Step Definitions

#### CategoryApiSteps.java
Cucumber step definitions for category API testing:
- Authentication steps
- API request steps
- Response validation steps
- Data verification steps

### 4. Feature File

#### categories_api.feature
Contains the Cucumber scenarios:
- **TC_API_CAT_USER_01**: Main test case for user retrieval of categories
- Additional test scenarios for comprehensive coverage

## Running the Tests

### Run All API Tests
```bash
mvn test -Dtest=ApiTestRunner
```

### Run Specific Test Case (TC_API_CAT_USER_01)
```bash
mvn test -Dtest=ApiTestRunner -Dcucumber.filter.tags="@TC_API_CAT_USER_01"
```

### Run All Category API Tests
```bash
mvn test -Dtest=ApiTestRunner -Dcucumber.filter.tags="@Categories"
```

### Run Smoke Tests Only
```bash
mvn test -Dtest=ApiTestRunner -Dcucumber.filter.tags="@Smoke"
```

### Run User-Level Tests
```bash
mvn test -Dtest=ApiTestRunner -Dcucumber.filter.tags="@User"
```

### Run Admin Tests
```bash
mvn test -Dtest=ApiTestRunner -Dcucumber.filter.tags="@Admin"
```

## Configuration

### config.properties
Update the following properties in `src/main/resources/config.properties`:

```properties
# Application URLs
base.url=http://localhost:8080

# User Credentials
admin.username=admin
admin.password=admin123
user.username=user
user.password=user123
```

## Dependencies

The following dependencies are configured in `pom.xml`:

### REST Assured (v5.3.2)
```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
</dependency>
```

### Cucumber (v7.14.0)
```xml
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-java</artifactId>
</dependency>
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-testng</artifactId>
</dependency>
```

### Jackson (v2.15.2)
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

## Test Reports

After running tests, reports are generated at:
- **HTML Report**: `target/cucumber-reports/api/cucumber.html`
- **JSON Report**: `target/cucumber-reports/api/cucumber.json`

## Troubleshooting

### Issue: Authentication Fails
**Solution**: Verify that:
1. Backend server is running on `http://localhost:8080`
2. Credentials in `config.properties` are correct
3. User accounts exist in the database

### Issue: Empty Category Array
**Solution**: 
1. Ensure categories exist in the system
2. Verify user has permission to view categories
3. Check database connection

### Issue: Test Cannot Find Step Definitions
**Solution**:
1. Verify package names match in `ApiTestRunner.java` glue configuration
2. Clean and rebuild: `mvn clean compile test-compile`

## API Endpoint Details

### GET /api/categories
**Description**: Retrieve all categories

**Headers**:
```
Authorization: Bearer {token}
Accept: application/json
```

**Response (200 OK)**:
```json
[
  {
    "id": 1,
    "name": "Indoor Plants",
    "parentCategory": null,
    "isMainCategory": true
  },
  {
    "id": 2,
    "name": "Succulents",
    "parentCategory": "Indoor Plants",
    "isMainCategory": false
  }
]
```

## Tags Reference

- `@api` - All API tests
- `@API` - Alternative API tag
- `@Categories` - Category-related tests
- `@TC_API_CAT_USER_01` - Specific test case
- `@Smoke` - Smoke test suite
- `@User` - User-level permission tests
- `@Admin` - Admin-level permission tests
- `@Negative` - Negative test scenarios

## Notes

1. The test uses **BDD (Behavior Driven Development)** approach with Cucumber
2. **Rest Assured** library handles HTTP requests and responses
3. All API responses are validated against expected structure
4. Tests are isolated and can run independently
5. Authentication tokens are generated fresh for each test scenario

## Support

For issues or questions, please refer to:
- Project documentation
- Cucumber documentation: https://cucumber.io/docs/
- Rest Assured documentation: https://rest-assured.io/
