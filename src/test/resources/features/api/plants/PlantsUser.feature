@API @Plants @User @TC_API_PLANTS_USER_01
Feature: User Plants API
  
  As a Regular User
  I want to retrieve plant information via API
  So that I can view available plants without admin privileges

  Background:
    Given user authentication token is available

  @TC_API_PLANTS_USER_01
  Scenario: Verify User can retrieve all plants via API
    Given user authentication token is available
    Given multiple plants exist in the system
    When user sends a GET request to "/api/plants"
    Then the plants response status code should be 200 
    And the response should contain a valid plants array
    And the response should contain at least 1 plant
    And each plant should have required fields

  @TC_API_PLANTS_USER_02
  Scenario: Verify User can retrieve specific plant by ID
    Given user authentication token is available
    Given plant exists with ID 2
    When user sends a GET request to "/api/plants/2"
    Then the plants response status code should be 200
    And the response should contain a single plant object
    And the plant should have the required fields

  @TC_API_PLANTS_USER_03
  Scenario: Verify API handles non-existent plant IDs for User
    Given user authentication token is available
    Given plant ID 999 does not exist in the system
    When user sends a GET request to "/api/plants/999"
    Then the plants response status code should be 404
    And the error message should indicate plant not found

  @TC_API_PLANTS_USER_04
  Scenario: Verify User can filter plants by category
    Given user authentication token is available
    Given category ID 8 exists with plants
    When user sends a GET request to "/api/plants/category/8"
    Then the plants response status code should be 200
    And the response should contain a valid plants array
    And the response should contain exactly 3 plants
    And all plants should have category ID 8
    And no plants should have different category IDs

  @TC_API_PLANTS_USER_05
  Scenario: Verify API handles non-existent category IDs
    Given user authentication token is available
    Given category ID 999 does not exist
    When user sends a GET request to "/api/plants/category/999"
    Then the plants response status code should be 404
    And the error message should indicate category not found or return empty array