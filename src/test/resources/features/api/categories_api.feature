@api @API @Categories
Feature: Category API Testing
  As a user of the Plant Sales system
  I want to interact with the Categories API
  So that I can manage and retrieve category information

  Background:
    Given multiple categories exist in the system

  @TC_API_CAT_USER_01 @Smoke @User
  Scenario: TC_API_CAT_USER_01 - Verify User can retrieve all categories via API
    Given I am authenticated as a regular user
    When I send a GET request to "/api/categories" with user credentials
    Then the response status code should be 200
    And the response should be a JSON array
    And the response should contain category objects
    And each category should contain field "id"
    And each category should contain field "name"
    And each category should contain field "parentCategory"
    And each category should contain field "isMainCategory"
    And the response should include both main and sub-categories
    And no admin-only fields should be present
    And I should be able to count the categories returned

  @TC_API_CAT_ADMIN_01 @Admin
  Scenario: Verify Admin can retrieve all categories via API
    Given I am authenticated as an admin
    When I send a GET request to "/api/categories" with admin credentials
    Then the response status code should be 200
    And the response should be a JSON array
    And the response should contain category objects
    And each category should have required fields

  @TC_API_CAT_USER_02 @Negative
  Scenario: Verify retrieving categories without authentication fails
    When I send a GET request to "/api/categories"
    Then the response status code should be 401

  @TC_API_CAT_USER_02 @User @Manual
  Scenario: TC_API_CAT_USER_02 - Verify User can retrieve specific category by ID
    # NOTE: This test requires a category with ID 3 to exist in the database
    # Update the category ID based on your test data
    Given I am authenticated as a regular user
    When I send a GET request to "/api/categories" with category ID 3
    Then the response status code should be 200
    And the response should be a single category object
    And the category should have id 3
    And each category should have required fields
    And no sensitive or admin fields should be exposed

  @TC_API_CAT_USER_03 @Negative @User
  Scenario: TC_API_CAT_USER_03 - Verify API handles non-existent category IDs for User
    Given I am authenticated as a regular user
    When I send a GET request to "/api/categories" with category ID 99999
    Then the response status code should be 404
    And the response should contain an error message
    And the error message should indicate "not found"

  @TC_API_CAT_USER_04 @User
  Scenario: TC_API_CAT_USER_04 - Verify User can retrieve only main categories
    Given I am authenticated as a regular user
    When I send a GET request to "/api/categories/main" for main categories
    Then the response status code should be 200
    And the response should be a JSON array
    And the response should contain category objects
    And all categories should have null parent category

  @TC_API_CAT_USER_05 @User
  Scenario: TC_API_CAT_USER_05 - Verify User can retrieve only sub-categories
    Given I am authenticated as a regular user
    When I send a GET request to "/api/categories/sub-categories" for sub-categories
    Then the response status code should be 200
    And the response should be a JSON array
    And the response should contain category objects
    And all categories should have populated parent category
