# UI Test Cases - Plant Management Module

## Test Case: TC_UI_PLANT_ADM_001

### Test Case Header
| Property | Value |
|----------|-------|
| **Test ID** | TC_UI_PLANT_ADM_001 |
| **Test Title** | Verify Admin can successfully add a new plant with valid data |
| **Module** | Plants Management |
| **Priority** | High |
| **Created By** | QA Team |
| **Created Date** | 2026-02-05 |
| **Status** | Ready for Execution |
| **Test Type** | Functional UI Test |
| **Automation Framework** | Selenium + Cucumber + TestNG |

---

## Test Case Details

### Test Summary
Verifies that an admin user can successfully add a new plant with valid inputs for Plant Name, Sub-category, Price, and Quantity. The test ensures that the system validates the data correctly, displays a success message upon saving, redirects the user back to the Plants List page, and displays the newly added plant in the list.

### Test Description
This test case validates the complete "Add Plant" workflow from the admin perspective. It covers:
- Navigation to Add Plant page
- Data entry validation
- Form submission
- Success confirmation
- Data persistence in the plant list

---

## Preconditions
1. User is logged in as an admin user
2. Admin is on the Plants List Page (`/ui/plants`)
3. Browser supports Bootstrap 5+ UI framework
4. Valid test data is available (Plant Name, Category, Price, Quantity)

---

## Test Steps and Expected Results

| Step # | Action | Expected Result | Priority |
|--------|--------|-----------------|----------|
| 1 | Click the **"Add a Plant"** button on the Plants List page | System navigates to the **"Add Plants"** page (`/ui/plants/add`) | Must Have |
| 2 | Enter "**Red Rose**" in the **Plant Name** field | Field accepts the value "Red Rose" (3-10 character length requirement met: 8 characters) | Must Have |
| 3 | Select **"Rose UK"** from the Category/Sub-category dropdown | Sub-category "Rose UK" is selected and displayed in the dropdown | Must Have |
| 4 | Enter "**2000**" in the **Price** field | Field accepts the value "2000" (Numeric value > 0) | Must Have |
| 5 | Enter "**12**" in the **Quantity** field | Field accepts the value "12" (Numeric, non-negative value) | Must Have |
| 6 | Click the **"Save"** button | Save button is clickable and form is submitted | Must Have |
| 7 | Validate success message is displayed | Success message "**Plant added successfully**" (or similar) is visible on the page | Must Have |
| 8 | Verify page redirect | User is redirected to the **Plants List page** (`/ui/plants`) | Must Have |
| 9 | Verify plant in list | The newly added plant **"Red Rose"** is present in the plants list with correct details: <br/> - Name: "Red Rose" <br/> - Category: "Rose UK" <br/> - Price: "2000.00" <br/> - Stock: "12" | Must Have |

---

## Test Data

| Test Data Field | Value | Data Type | Constraints |
|----------------|-------|-----------|-------------|
| Plant Name | Red Rose | String | 3-10 characters, alphanumeric with spaces |
| Sub-Category | Rose UK | String | Must be from predefined dropdown list |
| Price | 2000 | Numeric (Decimal) | Must be > 0, up to 2 decimal places |
| Quantity | 12 | Numeric (Integer) | Must be >= 0, no decimals |

### Valid Test Data Characteristics:
- **Plant Name**: Meets minimum (3) and maximum (10) character requirements
- **Category**: Exists in the category dropdown menu
- **Price**: Positive numeric value greater than zero
- **Quantity**: Non-negative integer value

---

## Expected Results Summary

### Functional Expected Results:
1. ✅ **Form Page Load**: Add Plants page renders successfully with all required fields
2. ✅ **Data Input**: System accepts all valid input data without errors
3. ✅ **Form Submission**: Save button successfully submits the form
4. ✅ **Success Notification**: Success message is displayed to the user
5. ✅ **Page Navigation**: System redirects to Plants List page after successful save
6. ✅ **Data Persistence**: Newly added plant appears in the plants list immediately

### Data Validation Expected Results:
- Plant Name field: Accepts 8-character string "Red Rose"
- Category field: Accepts "Rose UK" from dropdown options
- Price field: Accepts positive decimal "2000"
- Quantity field: Accepts non-negative integer "12"

### UI/UX Expected Results:
- Success message is styled as a success alert
- Plant details match the input provided
- Plant list is sorted/updated correctly
- No validation errors are displayed

---

## Test Execution Environment

| Configuration | Details |
|---------------|---------|
| **Browser** | Chrome / Firefox / Edge (Latest stable version) |
| **Browser Resolution** | 1920x1080 (Desktop view) |
| **OS** | Windows 10+ / macOS / Linux |
| **WebDriver** | Selenium WebDriver 4.15+ |
| **Wait Strategy** | Explicit waits (10 seconds default) |
| **Base URL** | As configured in testdata.properties |

---

## Automation Implementation

### Feature File (Cucumber/Gherkin)
**Location**: `src/test/resources/features/ui/plants/plant_crud.feature`

```gherkin
@ui @plants
Feature: Plant CRUD Operations
  As an admin user
  I want to manage plants in the system
  So that I can add, edit, and delete plants

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

### Page Object Class
**Location**: `src/test/java/com/example/ui/pages/plants/AddEditPlantPage.java`

**Key Methods**:
- `navigateToAddPlantPage(String baseUrl)` - Navigate to add plant page
- `enterPlantName(String plantName)` - Enter plant name
- `selectCategory(String categoryName)` - Select category from dropdown
- `enterPrice(String price)` - Enter price value
- `enterQuantity(String quantity)` - Enter quantity value
- `clickSaveButton()` - Click save button
- `isSuccessMessageDisplayed(String message)` - Verify success message

### Step Definitions
**Location**: `src/test/java/com/example/ui/stepDefinitions/plants/PlantCRUDSteps.java`

**Implemented Steps**:
- `userEntersPlantNameAs(String plantName)`
- `userSelectsSubCategoryAs(String categoryName)`
- `userEntersPriceAs(String price)`
- `userEntersQuantityAs(String quantity)`
- `aSuccessMessageShouldBeDisplayed(String expectedMessage)`
- `newlyAddedPlantShouldBeDisplayedInTheList(String plantName)`

---

## Test Execution Instructions

### Via Maven Command Line:
```bash
mvn clean test -Dtest=TestRunner -Dgroups="ui,plants"
```

### Via Maven with Specific Scenario:
```bash
mvn clean test -Dtest=UiTestRunner -Dcucumber.filter.tags="@TC_UI_PLANT_ADM_001"
```

### Prerequisites:
1. Maven 3.6+ installed
2. Java 11+ JDK
3. Selenium WebDriver 4.15+ (configured in pom.xml)
4. Valid configuration in `src/main/resources/config.properties`
5. Valid test credentials in `src/main/resources/testdata.properties`

---

## Test Criteria for Pass/Fail

### Test PASSES if:
1. ✅ Add plant page loads successfully
2. ✅ All input fields accept the test data without validation errors
3. ✅ Form submission is successful
4. ✅ Success message "Plant added successfully" is displayed
5. ✅ Page redirects to `/ui/plants`
6. ✅ "Red Rose" appears in the plants list with:
   - Category: "Rose UK"
   - Price: "2000.00"
   - Stock: "12"
7. ✅ No JavaScript errors in browser console
8. ✅ All steps complete within acceptable time (< 10 seconds per step)

### Test FAILS if:
1. ❌ Add plant page fails to load
2. ❌ Any input field rejects valid data with validation error
3. ❌ Form submission fails with error message
4. ❌ Success message is not displayed
5. ❌ Page doesn't redirect to plants list
6. ❌ Newly added plant doesn't appear in the list
7. ❌ Plant data in list doesn't match input data
8. ❌ Any step times out (> 10 seconds)

---

## Defects & Known Issues
| Defect ID | Description | Status | Impact |
|-----------|-------------|--------|--------|
| (If any found during testing) | | | |

---

## Test Results Template

| Execution # | Date | Tester | Browser | Result | Notes |
|-------------|------|--------|---------|--------|-------|
| 1 | YYYY-MM-DD | Name | Chrome 120 | PASS/FAIL | |
| 2 | YYYY-MM-DD | Name | Firefox 121 | PASS/FAIL | |

---

## Dependencies & Prerequisites
- ✅ [Login.feature](src/test/resources/features/ui/authentication/login.feature) - User must be logged in first
- ✅ [PlantListPage.java](src/test/java/com/example/ui/pages/plants/PlantListPage.java) - To verify plant in list
- ✅ [DriverManager.java](src/test/java/com/example/utils/DriverManager.java) - For WebDriver management
- ✅ Valid admin credentials in testdata.properties

---

## Related Test Cases
- **TC_UI_PLANT_ADM_002**: Verify Admin can edit an existing plant
- **TC_UI_PLANT_ADM_003**: Verify Admin can delete a plant
- **TC_UI_PLANT_VALIDATION_001**: Verify plant name validation (min/max length)
- **TC_UI_PLANT_VALIDATION_002**: Verify price validation (positive numbers only)
- **TC_UI_PLANT_VALIDATION_003**: Verify quantity validation (non-negative numbers)

---

## Notes & Remarks
- Test data uses realistic values that would typically be used in production
- Plant "Red Rose" with category "Rose UK" is confirmed to exist in the category dropdown
- Success message text may vary slightly (e.g., "Plant added successfully", "Plant saved") - test accepts partial matches
- After successful addition, the plant appears immediately in the list (no page refresh needed)
- Test is independent and can be run in any order

---

**Document Version**: 1.0  
**Last Updated**: 2026-02-05  
**Next Review**: 2026-02-12
