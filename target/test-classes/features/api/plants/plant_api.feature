@TC_API_PLANTS_ADM_01 @api @plants @admin
Feature: Plant Creation API - Admin
  As an Admin user
  I want to create plants under specific sub-categories
  So that I can manage the plant inventory

  @TC_API_PLANTS_ADM_01
  Scenario: Create a plant with valid data under a sub-category
    Given I have an admin authentication token
    When I create a new plant with the following details under sub-category "8":
      | name      | price | quantity |
      | ABC Plant | 150   | 25       |
    Then the response status code should be 201
    And the response should contain the created plant with:
      | name      | ABC Plant |
      | price     | 150.0     |
      | quantity  | 25        |
    And the plant should be assigned to category with id "8"
    And the category object should be present with id "8"

  @TC_API_PLANTS_ADM_02 @api @plants @admin @validation
  Scenario: Validate all required fields for plant creation
    Given I have an admin authentication token
    
    # Test missing name field
    When I create a plant with the following details under sub-category "8" without name field:
      | price | quantity |
      | 150   | 25       |
    Then the response status code should be 400
    And the error message should contain information about name being required
    
    # Test missing price field
    When I create a plant with the following details under sub-category "8" without price field:
      | name      | quantity |
      | Test Plant | 25       |
    Then the response status code should be 400
    And the error message should contain information about price being required
    
    # Test missing quantity field
    When I create a plant with the following details under sub-category "8" without quantity field:
      | name      | price |
      | Test Plant | 150   |
    Then the response status code should be 400
    And the error message should contain information about quantity being required
    
    # Test empty name field
    When I create a plant with the following details under sub-category "8" with empty name:
      | name | price | quantity |
      |      | 150   | 25       |
    Then the response status code should be 400
    And the error message should contain information about name being required

  @TC_API_PLANTS_USER_06 @api @plants @user @pagination
  Scenario: User can access paginated plants with default parameters
    Given I have a user authentication token
    When I request the paginated plants list with default parameters
    Then the response status code should be 200
    And the response should contain paginated plant data
    And the total elements should be greater than or equal to 25
    And the total pages should be greater than 0
    And the page number should be 0
    And the page size should be greater than 0

  @TC_API_PLANTS_USER_07 @api @plants @user @pagination
  Scenario: User can control pagination parameters
    Given I have a user authentication token
    When I request the paginated plants list with page "1" and size "10"
    Then the response status code should be 200
    And the response should contain paginated plant data
    And the content size should be less than or equal to 10
    And the page number should be 1
    And the page size should be 10
    When I request the paginated plants list with page "2" and size "5"
    Then the response status code should be 200
    And the content size should be less than or equal to 5
    And the page number should be 2
    And the page size should be 5
    And the total elements should be the same in both responses

  @TC_API_PLANTS_USER_08 @api @plants @user @sorting
  Scenario: User can sort plants by name via API
    Given I have a user authentication token
    When I request plants sorted by "name" in "asc" order
    Then the response status code should be 200
    And the response should contain sorted plant data
    And the plant names should be in ascending order
    When I request plants sorted by "name" in "desc" order
    Then the response status code should be 200
    And the plant names should be in descending order
    And the first plant in ascending should be different from first plant in descending

  @TC_API_PLANTS_USER_09 @api @plants @user @sorting
  Scenario: User can sort plants by price via API
    Given I have a user authentication token
    When I request plants sorted by "price" in "asc" order
    Then the response status code should be 200
    And the response should contain sorted plant data
    And the plant prices should be in ascending numeric order
    When I request plants sorted by "price" in "desc" order
    Then the response status code should be 200
    And the plant prices should be in descending numeric order
    And the first price in ascending should be less than or equal to first price in descending

  @TC_API_PLANTS_USER_10 @api @plants @user @sorting
  Scenario: User can sort plants by stock quantity via API
    Given I have a user authentication token
    When I request plants sorted by "quantity" in "asc" order
    Then the response status code should be 200
    And the response should contain sorted plant data
    And the plant quantities should be in ascending order from lowest to highest
    When I request plants sorted by "quantity" in "desc" order
    Then the response status code should be 200
    And the plant quantities should be in descending order from highest to lowest
    And the lowest quantity in ascending should be less than or equal to highest quantity in descending

  @TC_API_PLANTS_USER_11 @api @plants @user @search
  Scenario: User can search plants by name via API
    Given I have a user authentication token
    When I search for plants with name "monstera"
    Then the response status code should be 200
    And the response should contain search results
    And all returned plant names should contain "monstera"
    When I search for plants with name "MONSTERA"
    Then the response status code should be 200
    And the search should be case-insensitive
    When I search for plants with name "plant"
    Then the response status code should be 200
    And all returned plant names should contain "plant"
    When I search for plants with name "xyznonexistent"
    Then the response status code should be 200
    And the total elements should be 0

  @TC_API_PLANTS_USER_12 @api @plants @user @filter
  Scenario: User can filter plants by category via API
    Given I have a user authentication token
    When I filter plants by category "Indoor"
    Then the response status code should be 200
    And the response should contain filtered plant data
    And all returned plants should belong to "Indoor" category
    When I filter plants by category "Outdoor"
    Then the response status code should be 200
    And all returned plants should belong to "Outdoor" category
    And the Indoor and Outdoor results should be different
    When I filter plants by category "NonExistentCategory"
    Then the response status code should be 200
    And the total elements should be greater than or equal to 0