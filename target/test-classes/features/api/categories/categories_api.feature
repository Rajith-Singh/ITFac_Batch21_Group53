@TC_API_CATEGORIES_01 @api @categories
Feature: Categories API
  As a user of the plant management system
  I want to manage categories via API
  So that I can organize plants into proper categories

  @TC_API_CATEGORIES_USER_01 @api @categories @user @retrieval
  Scenario: User can retrieve all categories
    Given I have a user authentication token
    When I request the categories list
    Then the response status code should be 200
    And the response should contain a list of categories
    And each category should contain id and name information

  @TC_API_CATEGORIES_USER_02 @api @categories @user @retrieval
  Scenario: User can retrieve a specific category by ID
    Given I have a user authentication token
    And I have a valid category ID "1"
    When I request the category with ID "1"
    Then the response status code should be 200
    And the response should contain the category details
    And the category should have id "1"
    And the category should have a name
    And the category may contain sub-categories

  @TC_API_CATEGORIES_ADM_01 @api @categories @admin @create
  Scenario: Admin can create a new category with valid data
    Given I have an admin authentication token
    When I create a new category with the following details:
      | name        | description           |
      | Test Category | A category for testing |
    Then the response status code should be 201
    And the response should contain the created category
    And the category name should be "Test Category"
    And the category description should be "A category for testing"

  @TC_API_CATEGORIES_ADM_02 @api @categories @admin @create @validation
  Scenario: Category validation for required fields
    Given I have an admin authentication token
    
    # Test missing name
    When I create a category with the following details without name field:
      | description           |
      | A category for testing |
    Then the response status code should be 400
    And the error message should indicate that name is required
    
    # Test empty name
    When I create a category with the following details with empty name:
      | name | description           |
      |      | A category for testing |
    Then the response status code should be 400
    And the error message should indicate that name is required

  @TC_API_CATEGORIES_ADM_03 @api @categories @admin @create @validation
  Scenario: Category validation for uniqueness
    Given I have an admin authentication token
    And there is an existing category with name "Existing Category"
    When I create a new category with name "Existing Category"
    Then the response status code should be 409
    And the error message should indicate that category name must be unique

  @TC_API_CATEGORIES_ADM_04 @api @categories @admin @update
  Scenario: Admin can update an existing category
    Given I have an admin authentication token
    And I have an existing category with ID "1"
    When I update the category with the following details:
      | name        | description              |
      | Updated Name | Updated description text |
    Then the response status code should be 200
    And the response should contain the updated category
    And the category name should be "Updated Name"
    And the category description should be "Updated description text"

  @TC_API_CATEGORIES_ADM_05 @api @categories @admin @update @validation
  Scenario: Category update validation
    Given I have an admin authentication token
    And I have an existing category with ID "1"
    
    # Test with empty name
    When I update the category with empty name
    Then the response status code should be 400
    And the error message should indicate that name is required

  @TC_API_CATEGORIES_ADM_06 @api @categories @admin @delete
  Scenario: Admin can delete a category
    Given I have an admin authentication token
    And I have an existing category with ID "1"
    And the category has no associated plants
    When I delete the category with ID "1"
    Then the response status code should be 204
    When I request the category with ID "1"
    Then the response status code should be 404

  @TC_API_CATEGORIES_ADM_07 @api @categories @admin @delete @validation
  Scenario: Category deletion validation
    Given I have an admin authentication token
    
    # Test deleting non-existent category
    When I attempt to delete a category with ID "99999"
    Then the response status code should be 404
    
    # Test deleting category with associated plants
    Given I have a category with ID "1" that has associated plants
    When I attempt to delete the category with ID "1"
    Then the response status code should be 409
    And the error message should indicate that category cannot be deleted due to associated plants

  @TC_API_CATEGORIES_USER_03 @api @categories @user @filtering
  Scenario: User can filter categories by name
    Given I have a user authentication token
    And there are multiple categories with different names
    When I search for categories with name containing "Indoor"
    Then the response status code should be 200
    And the response should contain categories matching the search criteria
    And all returned category names should contain "Indoor"

  @TC_API_CATEGORIES_USER_04 @api @categories @user @pagination
  Scenario: User can paginate categories results
    Given I have a user authentication token
    And there are multiple categories in the system
    When I request categories with page "0" and size "5"
    Then the response status code should be 200
    And the response should contain paginated categories data
    And the content size should be less than or equal to 5
    And the categories page number should be 0
    And the categories page size should be 5

  @TC_API_CATEGORIES_USER_05 @api @categories @user @subcategories
  Scenario: Categories can contain sub-categories
    Given I have a user authentication token
    And I have a parent category with sub-categories
    When I request the parent category details
    Then the response status code should be 200
    And the category should contain sub-categories information
    And each sub-category should have its own id and name