# Test Automation Implementation for TC_UI_PLANT_ADM_001

## Overview
Comprehensive UI automation test implementation for the test case: **TC_UI_PLANT_ADM_001 - Verify Admin can successfully add a new plant with valid data**

## Test Case Details
- **Test ID**: TC_UI_PLANT_ADM_001
- **Title**: Verify Admin can successfully add a new plant with valid data
- **Objective**: Verifies that an admin user can successfully add a new plant with valid inputs for Plant Name, Sub-category, Price, and Quantity

## Implementation Details

### 1. Feature File: [plant_crud.feature](src/test/resources/features/ui/plants/plant_crud.feature)
```gherkin
@ui @plants
Feature: Plant CRUD Operations
  Background:
    Given user is logged in as admin
    And user is on the Plants List page

  @TC_UI_PLANT_ADM_001
  Scenario: Verify Admin can successfully add a new plant with valid data
    When user clicks on "Add a Plant" button
    Then user should be navigated to "Add Plants" page
    When user enters plant name as "Red Rose"
    And user selects sub category as "Rose UK"
    And user enters price as "2000"
    And user enters quantity as "12"
    And user clicks on "save" button
    Then a success message "Plant added successfully" should be displayed
    And user should be redirected to "/ui/plants" page
    And newly added plant "Red Rose" should be displayed in the plants list
```

### 2. Page Object Classes

#### [LoginPage.java](src/test/java/com/example/ui/pages/login/LoginPage.java)
Handles login functionality:
- `navigateToLoginPage()` - Navigate to login page
- `enterEmail()` - Enter email address
- `enterPassword()` - Enter password
- `loginAsAdmin()` - Complete login flow for admin

#### [PlantListPage.java](src/test/java/com/example/ui/pages/plants/PlantListPage.java)
Handles Plants List page interactions:
- `navigateToPlantsList()` - Navigate to plants list
- `clickAddPlantButton()` - Click "Add a Plant" button
- `isPlantListPageDisplayed()` - Verify plants list page is displayed
- `isPlantDisplayedInList()` - Verify plant exists in the list
- `getCurrentUrl()` - Get current URL

#### [AddEditPlantPage.java](src/test/java/com/example/ui/pages/plants/AddEditPlantPage.java)
Handles Add/Edit Plant page interactions:
- `navigateToAddPlantPage()` - Navigate to add plant page
- `isAddPlantPageDisplayed()` - Verify add plant page is displayed
- `enterPlantName()` - Enter plant name
- `selectCategory()` - Select category/subcategory
- `enterPrice()` - Enter plant price
- `enterQuantity()` - Enter plant quantity
- `clickSaveButton()` - Click save button
- `isSuccessMessageDisplayed()` - Verify success message
- `getSuccessMessage()` - Get success message text

### 3. Step Definition Classes

#### [LoginSteps.java](src/test/java/com/example/ui/stepDefinitions/login/LoginSteps.java)
Implements Given steps:
- `Given user is logged in as admin` - Login as admin user
- `Given user is on the Plants List page` - Navigate to plants list

#### [PlantListSteps.java](src/test/java/com/example/ui/stepDefinitions/plants/PlantListSteps.java)
Implements When/Then steps for Plants List:
- `When user clicks on "Add a Plant" button`
- `Then user should be navigated to "Add Plants" page`
- `Then user is redirected to "[url]" page`
- `Then newly added plant "[plantName]" should be displayed in the plants list`

#### [PlantCRUDSteps.java](src/test/java/com/example/ui/stepDefinitions/plants/PlantCRUDSteps.java)
Implements When/Then steps for CRUD operations:
- `When user enters plant name as "[name]"`
- `When user selects sub category as "[category]"`
- `When user enters price as "[price]"`
- `When user enters quantity as "[quantity]"`
- `When user clicks on "save" button`
- `Then a success message "[message]" should be displayed`
- `Then user should be redirected to "/ui/plants" page`

### 4. Utility Classes

#### [DriverManager.java](src/test/java/com/example/utils/DriverManager.java)
Manages WebDriver lifecycle:
- `initializeDriver()` - Initialize WebDriver (Chrome/Firefox)
- `getDriver()` - Get current WebDriver instance
- `quitDriver()` - Quit WebDriver and clean up

### 5. Hooks Configuration

#### [Hooks.java](src/test/java/com/example/hooks/Hooks.java)
Manages test setup and teardown:
- `@Before("@ui")` - Initialize WebDriver before UI tests
- `@After("@ui")` - Quit WebDriver and capture screenshots on failure

## Test Flow

1. **Setup**: Hooks initialize ChromeDriver
2. **Login**: Admin user logs in with credentials
3. **Navigate**: User navigates to Plants List page
4. **Click Add Button**: User clicks "Add a Plant" button
5. **Fill Form**: User enters plant details:
   - Plant Name: Red Rose
   - Category: Rose UK
   - Price: 2000
   - Quantity: 12
6. **Save**: User clicks Save button
7. **Verify**: Test verifies:
   - Success message is displayed
   - User is redirected to /ui/plants page
   - New plant appears in the plants list
8. **Teardown**: Hooks quit WebDriver

## Technologies Used
- **Framework**: Cucumber (BDD)
- **Language**: Java
- **WebDriver**: Selenium WebDriver
- **Page Object Model**: Yes (POM)
- **Waits**: WebDriverWait with explicit waits
- **Assertions**: Java assert statements

## Execution
To run this test:
```bash
mvn clean test -Dtags="@TC_UI_PLANT_ADM_001"
```

Or run all UI plants tests:
```bash
mvn clean test -Dtags="@ui and @plants"
```

## Locators Used
- Email Field: `id="email"`
- Password Field: `id="password"`
- Add Plant Button: `xpath="//a[contains(@href, '/ui/plants/add') and contains(text(), 'Add a Plant')]"`
- Plant Name Field: `id="name"`
- Category Dropdown: `id="categoryId"`
- Price Field: `id="price"`
- Quantity Field: `id="quantity"`
- Save Button: `xpath="//button[contains(text(), 'save') or contains(text(), 'Save')]"`
- Success Message: `xpath="//div[contains(@class, 'alert alert-success')]"`
- Plants Table: `xpath="//table[@class='table table-striped table-bordered align-middle']"`

## Configuration
- **Base URL**: http://localhost:8080
- **Admin Credentials**: admin@example.com / admin123
- **Browser**: Chrome (default)
- **Implicit Wait**: 10 seconds
- **Test Data**: Red Rose | Rose UK | 2000 | 12

## Notes
- All step definitions include appropriate waits for element visibility and interactability
- Success message verification checks both for exact message and generic "successfully" text
- Plant verification in list uses XPath with text contains to handle dynamic content
- Screenshots are captured automatically on test failure
